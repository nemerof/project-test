package com.example.communication.controller;

import com.example.communication.model.CommunicationUser;
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

  @GetMapping("/main")
  public String mainPage(Map<String, Object> model){
    CommunicationUser user1 = new CommunicationUser("Nama1", "123456", "somemail@gmail.com");
    CommunicationUser user2 = new CommunicationUser("Nama2", "654321", "somemail2@gmail.com");
    repository.saveAll(Arrays.asList(user1, user2));
    model.put("users", repository.findAll());
    return "main";
  }
}
