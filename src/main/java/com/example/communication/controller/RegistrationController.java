package com.example.communication.controller;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RegistrationController {
  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder encoder;

  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(
      @Valid User user,
      BindingResult bindingResult,
      @RequestParam("profilePicture") MultipartFile file, Model model) throws IOException {

    model.addAttribute("user", user);
    if (bindingResult.hasErrors()) {
      model.mergeAttributes(getErrors(bindingResult));
      return "registration";
    }

    boolean emailExist = false;
    List<User> users = repository.findAll();

    for (User user1 : users) {
      if (user1.getEmail().equals(user.getEmail())) {
        emailExist = true;
        break;
      }
    }

    if (emailExist) {
      model.addAttribute("emailExists", "User with such email exists!");
      return "registration";
    }

    User userFromDb = repository.findByUsername(user.getUsername());

    if (userFromDb != null) {
      model.addAttribute("usernameExists", "User exists!");
      return "registration";
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    user.setPassword(encoder.encode(user.getPassword()));
    ControllerUtils.savePhoto(file, user);
    return "redirect:/login";
  }

  static Map<String, String> getErrors(BindingResult bindingResult) {
    Map<String, String> errors = new HashMap<>();

    for (FieldError fieldError : bindingResult.getFieldErrors()) {
      String name = fieldError.getField() + "Error";
      String message = fieldError.getDefaultMessage();
      if (!errors.containsKey(name)) {
        errors.put(name, message);
      }
      if (errors.containsKey(name) && errors.get(name).contains("should") && message != null && message.contains("cannot")) {
        errors.replace(name, message);
      }
    }
    return errors;
  }
}
