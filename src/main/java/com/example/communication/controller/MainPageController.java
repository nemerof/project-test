package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class MainPageController {

  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/users")
  public String mainPage(Model model){
    User user1 = new User("Name1", "123456", "somemail@gmail.com", true);
    User user2 = new User("Name2", "654321", "somemail2@gmail.com", true);
    userRepository.saveAll(Arrays.asList(user1, user2));
    model.addAttribute("users", userRepository.findAll());
    return "users";
  }

  @GetMapping("/main")
  public String main(@RequestParam(required = false, defaultValue = "") String filter, Model model) {
    Iterable<Message> messages;
    if (filter != null && !filter.isEmpty()) {
      messages = messageRepository.findByTextContains(filter);
    } else {
      messages = messageRepository.findAll();
    }
    model.addAttribute("messages", messages);
    model.addAttribute("filter", "");
    return "main";
  }

  @PostMapping("/main")
  public String add(
      @RequestParam(required = false, defaultValue = "") String filter,
      @AuthenticationPrincipal User user,
      @RequestParam String text, Model model,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    Message message = new Message(text, user);

    if (file != null && !file.getOriginalFilename().isEmpty()) {
      File uploadDir = new File(uploadPath);

      if (!uploadDir.exists()) {
        uploadDir.mkdir();
      }

      String uuidFile = UUID.randomUUID().toString();
      String resultFilename = uuidFile + "." + file.getOriginalFilename();

      file.transferTo(new File(uploadPath + "/" + resultFilename));

      message.setFilename(resultFilename);
    }

    messageRepository.save(message);
    model.addAttribute("messages", messageRepository.findAll());
    model.addAttribute("filter", "");
    return "main";
  }

  @GetMapping("/delete/{id}")
  public String delete(
      @PathVariable(value="id") Long id, Model model
  ) {
    messageRepository.deleteById(id);
    model.addAttribute("messages", messageRepository.findAll());
    model.addAttribute("filter", "");
    return "redirect:/main";
  }
}
