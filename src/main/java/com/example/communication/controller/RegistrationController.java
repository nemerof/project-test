package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.model.Role;
import com.example.communication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Collections;
import java.util.Map;

@Controller
public class RegistrationController {
  @Autowired
  private UserRepository repository;

  @GetMapping("/registration")
  public String registration() {
    return "registration";
  }

  @PostMapping("/registration")
  public String addUser(User user, Map<String, Object> model) {
    User userFromDb = repository.findByUsername(user.getUsername());

    if (userFromDb != null) {
      model.put("message", "User exists!");
      return "registration";
    }
    user.setActive(true);
    user.setRoles(Collections.singleton(Role.USER));
    repository.save(user);

    return "redirect:/login";
  }
}