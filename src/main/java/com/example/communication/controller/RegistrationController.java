package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.RegistrationService;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class RegistrationController {

    private final PasswordEncoder encoder;

    private final UserRepository repository;

    private final RegistrationService registrationService;

    public RegistrationController(PasswordEncoder encoder, UserRepository repository, RegistrationService registrationService) {
        this.encoder = encoder;
        this.repository = repository;
        this.registrationService = registrationService;
    }

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User user,
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

        if (!registrationService.addUser(user, file, encoder)) {
            model.addAttribute("usernameExists", "User exists!");
            return "registration";
        }
        return "redirect:/login";
    }

    private static Map<String, String> getErrors(BindingResult bindingResult) {
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

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = registrationService.activateUser(code);

        if (isActivated) {
            model.addAttribute("message", "User successfully activated");
        } else {
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
