package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.model.Role;
import com.example.communication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
public class RegistrationController {
  @Autowired
  private UserRepository repository;

  @GetMapping("/registration")
  public String registration() {
    repository.save(new User("Name", "12345", "somemail@gmail.com", true,
        Collections.singleton(Role.ADMIN)));
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
    model.addAttribute("user", user);
    if (bindingResult.hasErrors()) {
      model.mergeAttributes(getErrors(bindingResult));
      return "registration";
    }

    User userFromDb = repository.findByUsername(user.getUsername());

    if (userFromDb != null) {
      model.addAttribute("message", "User exists!");
      return "registration";
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    repository.save(user);

    return "redirect:/login";
  }

  static Map<String, String> getErrors(BindingResult bindingResult) {
    Collector<FieldError, ?, Map<String, String>> collector = Collectors.toMap(
            fieldError -> fieldError.getField() + "Error",
            FieldError::getDefaultMessage
    );
    return bindingResult.getFieldErrors().stream().collect(collector);
  }
}