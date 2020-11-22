package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ProfileController {

  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/profile/{id}")
  public String profile(@PathVariable(value="id") Long id, Model model){
    Iterable<Message> userMessages = messageRepository.findByUserId(id);
    model.addAttribute("messages", userMessages);
    return "profile";
  }
}
