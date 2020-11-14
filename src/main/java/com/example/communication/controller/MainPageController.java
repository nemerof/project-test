package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.util.Arrays;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainPageController {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/users")
  public String mainPage(Map<String, Object> model){
    User user1 = new User("Name1", "123456", "somemail@gmail.com", true);
    User user2 = new User("Name2", "654321", "somemail2@gmail.com", true);
    userRepository.saveAll(Arrays.asList(user1, user2));
    model.put("users", userRepository.findAll());
    return "users";
  }

  @GetMapping("/main")
  public String main(Map<String, Object> model) {
//    Message message1 = new Message("Message 1", new User("Name1", "123456", "somemail@gmail.com", true));
//    Message message2 = new Message("Message 2", new User("Name2", "654321", "somemail2@gmail.com", true));
//    messageRepository.saveAll(Arrays.asList(message1, message2));
    model.put("messages", messageRepository.findAll());
    return "main";
  }

  @PostMapping("/main")
  public String add(
      @AuthenticationPrincipal User user,
      @RequestParam String text, Map<String, Object> model
  ) {
    Message message = new Message(text, user);
    messageRepository.save(message);
    model.put("messages", messageRepository.findAll());
    return "main";
  }

  @GetMapping("filter")
  public String filter(@RequestParam String filter, Map<String, Object> model) {
    Iterable<Message> messages;

    if (filter != null && !filter.isEmpty()) {
      messages = messageRepository.findByTextContains(filter);
    } else {
      messages = messageRepository.findAll();
    }
    model.put("messages", messages);
    return "main";
  }
}
