package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import com.example.communication.service.UserService;
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

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @GetMapping("/profile/{id}")
  public String profile(
          @AuthenticationPrincipal User currentUser,
          @PathVariable(value="id") Long id,
          Model model){
    Iterable<Message> userMessages =
            messageRepository.findByUserId(id);
    User user = userRepository.findById(id).get();
    model.addAttribute("profileName", user.getUsername());
    model.addAttribute("messages", userMessages);
    model.addAttribute("subscribers", user.getSubscribers().size());
    model.addAttribute("subscriptions", user.getSubscriptions().size());
    model.addAttribute("profileId", user.getId());
    model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
    model.addAttribute("isCurrentUser", currentUser.equals(user));
    return "profile";
  }

  //Надо что-то сделать
  @PostMapping("/profile/{id}")
  public String add(
          @RequestParam("file") MultipartFile file,
          @PathVariable(value="id") Long id,
          @RequestParam(required = false, defaultValue = "") String filter,
          @AuthenticationPrincipal User user,
          @RequestParam String text, Model model
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

  @GetMapping("/edit")
  public String edit(
          @AuthenticationPrincipal User user, Model model
  ) {
    model.addAttribute("username", user.getUsername());
//    model.addAttribute("password", user.getPassword());
//    model.addAttribute("profilePic", user.getProfilePic());
    return "editUser";
  }

  @PostMapping("/edit")
  public String edit(
          @AuthenticationPrincipal User user,
          @RequestParam String username
//      @RequestParam(required = false, defaultValue = "/static/images/default-profile-icon.png") String profilePic,
  ) {
    user.setUsername(username);
    userRepository.save(user);
//    user.setProfilePic(profilePic);
    return "redirect:/edit";
  }

  @GetMapping("profile/subscribers/{id}")
  public String getSubscribers(@PathVariable(value = "id") Long id,
                               Model model) {
    User user = userRepository.findById(id).get();
    model.addAttribute("subscribers", user.getSubscribers());
    return "subscribers";
  }

  @GetMapping("profile/subscriptions/{id}")
  public String getSubscriptions(@PathVariable(value = "id") Long id,
                                 Model model) {
    User user = userRepository.findById(id).get();
    model.addAttribute("subscriptions", user.getSubscriptions());
    return "subscriptions";
  }

  @GetMapping("profile/subscribe/{id}")
  public String subscribe(@PathVariable(value = "id") Long id,
                          @AuthenticationPrincipal User currentUser) {
    User user = userRepository.findById(id).get();
    userService.subscribe(currentUser, user);
    return "redirect:/profile/"+id;
  }

  @GetMapping("profile/unsubscribe/{id}")
  public String unsubscribe(@PathVariable(value = "id") Long id,
                            @AuthenticationPrincipal User currentUser) {
    User user = userRepository.findById(id).get();
    userService.unsubscribe(currentUser, user);
    return "redirect:/profile/"+id;
  }
}