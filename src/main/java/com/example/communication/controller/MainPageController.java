package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.*;

import com.example.communication.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@Controller
public class MainPageController {

  @Value("${upload.path}")
  private String uploadPath;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private MessageService messageService;

  @GetMapping("/users")
  public String mainPage(Model model){
    User user1 = new User("Name1", "123456", "somemail@gmail.com", true);
    User user2 = new User("Name2", "654321", "somemail2@gmail.com", true);
    userRepository.saveAll(Arrays.asList(user1, user2));
    model.addAttribute("users", userRepository.findAll());
    return "users";
  }

  @GetMapping("/")
  public String main(@AuthenticationPrincipal User user,
                     @RequestParam(required = false, defaultValue = "") String filter,
                     Model model) {
    List<MessageDTO> messages = messageService.getAllMessages(filter, user);

    model.addAttribute("messages", messages);
    model.addAttribute("filter", "");
    return "main";
  }

  @PostMapping("/")
  public String add(
      @RequestParam(required = false, defaultValue = "") String filter,
      @AuthenticationPrincipal User user,
      @RequestParam String text, Model model,
      @RequestParam("file") MultipartFile file
  ) throws IOException {
    Message message = new Message(text, user);
    ControllerUtils.savePhoto(file, message);

    model.addAttribute("messages", messageService.getAllMessages(filter, user));
    model.addAttribute("filter", "");
    return "main";
  }

  @GetMapping("/delete/{id}")
  public String delete(
      @PathVariable(value="id") Long id, Model model
  ) {
    ControllerUtils.deleteMessage(id);

    return "redirect:/";
  }

  @GetMapping("/messages/{message}/like")
  public String like(
          @AuthenticationPrincipal User currentUser,
          @PathVariable Message message,
          RedirectAttributes redirectAttributes,
          @RequestHeader(required = false) String referer
  ) {
    Set<User> likes = message.getLikes();

    if (likes.contains(currentUser)) {
      likes.remove(currentUser);
    } else {
      likes.add(currentUser);
    }

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

    components.getQueryParams()
            .entrySet()
            .forEach(pair -> redirectAttributes.addAttribute(pair.getKey(), pair.getValue()));

    return "redirect:" + components.getPath();
  }

  @GetMapping("/login")
  public void loginPage(@RequestParam(required = false, name = "error") String error, Model model) {
    if (error != null)
      model.addAttribute("errorUsernamePassword", "Username or password is incorrect!");
  }

  @PostMapping("/login")
  public String loginIn() {
    return "login";
  }
}
