package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.List;

import freemarker.ext.beans.StringModel;
import freemarker.template.SimpleScalar;
import freemarker.template.TemplateMethodModelEx;
import freemarker.template.TemplateModelException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class ProfileController {

  private final MessageRepository messageRepository;

  private final UserRepository userRepository;

  private final UserService userService;

  public ProfileController(MessageRepository messageRepository,
      UserRepository userRepository, UserService userService) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.userService = userService;
  }

  @GetMapping("/profile/{id}")
  public String profile(
          @AuthenticationPrincipal User currentUser,
          @PathVariable(value="id") Long id,
          Model model){
    User user = userRepository.findById(id).get();
    List<MessageDTO> userMessages =
            messageRepository.findByUserId(currentUser, user);
    for (Message repost : user.getReposts())
      userMessages.add(new MessageDTO(repost, (long) repost.getLikes().size(), true));
    model.addAttribute("profileName", user.getUsername());
    model.addAttribute("messages", userMessages);
    model.addAttribute("subscribers", user.getSubscribers().size());
    model.addAttribute("subscriptions", user.getSubscriptions().size());
    model.addAttribute("profileId", user.getId());
    model.addAttribute("curProfPic", user.getProfilePic());
    model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
    model.addAttribute("isCurrentUser", currentUser.equals(user));
    model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
    model.addAttribute("formatDateTime", new FormatDateTimeMethodModel());
    return "profile";
  }

  @PostMapping("/profile/{id}")
  public String add(
          @RequestParam("file") MultipartFile file,
          @PathVariable(value="id") Long id,
          @AuthenticationPrincipal User user,
          @RequestParam String text, Model model
  ) throws IOException {
    Message message = new Message(text, user);
    ControllerUtils.saveMessage(file, message);
    model.addAttribute("filter", "");
    return "redirect:/profile/"+id;
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

  @GetMapping("/repost/{messageId}")
  public String repost(@PathVariable Long messageId,
                       @AuthenticationPrincipal User currentUser,
                       RedirectAttributes redirectAttributes,
                       @RequestHeader(required = false) String referer) {
    currentUser.getReposts().add(messageRepository.findById(messageId).get());

    try {
      userRepository.save(currentUser);
    } catch (Exception e) {
      System.err.println("U can repost only one time");
      UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

      components.getQueryParams()
              .forEach(redirectAttributes::addAttribute);
      return "redirect:" + components.getPath();
    }

    return "redirect:/messages/"+messageId+"/like";
  }
}