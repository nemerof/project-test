package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import java.io.File;
import java.io.IOException;
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
public class ProfileController {

  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  private MessageRepository messageRepository;

  @GetMapping("/profile/{id}")
  public String profile(@PathVariable(value="id") Long id, Model model){
    Iterable<Message> userMessages = messageRepository.findByUserId(id);
    model.addAttribute("messages", userMessages);
    return "profile";
  }

  //Надо что-то сделать
  @PostMapping("/profile/{id}")
  public String add(
      @PathVariable(value="id") Long id,
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
    return "redirect:/profile/"+id;
  }
}
