package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainPageController {

  @Autowired
  private UserRepository repository;

  @GetMapping("/")
  public String mainPage(Map<String, Object> model){
    User user1 = new User("Name1", "123456", "somemail@gmail.com", true);
    User user2 = new User("Name2", "654321", "somemail2@gmail.com", true);
    repository.saveAll(Arrays.asList(user1, user2));
    model.put("users", repository.findAll());
    return "main";
  }
}
