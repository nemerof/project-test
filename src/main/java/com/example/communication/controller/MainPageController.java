package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.MessageService;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Controller
public class MainPageController {

  private final MessageService messageService;

  private final MessageRepository messageRepository;

  private final UserRepository userRepository;

  private final JdbcTemplate jdbcTemplate;

  public MainPageController(MessageService messageService, MessageRepository messageRepository,
                            UserRepository userRepository, JdbcTemplate jdbcTemplate) {
    this.messageService = messageService;
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping("/users")
  public String mainPage(Model model){
    return "users";
  }

  @GetMapping("/")
  public String main(
      @AuthenticationPrincipal User user,
      @RequestParam(required = false, defaultValue = "") String filter,
      Model model,
      @PageableDefault(sort = { "postTime" }, direction = Sort.Direction.ASC, size = 5) Pageable pageable
  ) {
    Set<User> users = user.getSubscriptions();
    Page<MessageDTO> messages = messageService.getMainPageMessages(filter, user, users, pageable);

    model.addAttribute("url", "/");
    model.addAttribute("loginUserId", user.getId());
    model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
    model.addAttribute("messages", messages);
    model.addAttribute("formatDateTime", new FormatDateTimeMethodModel());
    model.addAttribute("filter", filter);
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
    ControllerUtils.saveMessage(file, message);
    model.addAttribute("filter", filter);
    return "redirect:/";
  }

  @GetMapping("/delete/{id}")
  public String delete(
      @AuthenticationPrincipal User currentUser,
      @PathVariable(value="id") Long id,
      RedirectAttributes redirectAttributes,
      @RequestHeader(required = false) String referer
  ) {
    User user = userRepository.findById(currentUser.getId()).get();
    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
    components.getQueryParams()
            .forEach(redirectAttributes::addAttribute);

    Message message = messageRepository.findById(id).get();
    boolean isMessageRepost = !currentUser.getId().equals(message.getUser().getId()) && !user.getRoles().contains(Role.ADMIN);

    if (isMessageRepost) {
      try {
        user.getReposts().remove(message);
      } catch (Exception ignored) { }

      userRepository.save(user);

      return "redirect:/messages/" + id + "/dislike";
    } else {
      ControllerUtils.deleteMessage(id, user);
      return "redirect:" + components.getPath();
    }

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
        .forEach(redirectAttributes::addAttribute);
    return "redirect:" + components.getPath();
  }

  @GetMapping("/messages/{message}/dislike")
  public String dislike(
          @AuthenticationPrincipal User currentUser,
          @PathVariable Message message,
          RedirectAttributes redirectAttributes,
          @RequestHeader(required = false) String referer
  ) {
    Set<User> likes = message.getLikes();
    likes.remove(currentUser);

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

    components.getQueryParams()
            .forEach(redirectAttributes::addAttribute);
    return "redirect:" + components.getPath();
  }

  @GetMapping("/login")
  public void loginPage(@RequestParam(required = false, name = "error") String error, Model model) {
    if (error != null)
      model.addAttribute("errorUsernamePassword", "Username or password is incorrect/You need to activate your account");
  }

  @PostMapping("/login")
  public String loginIn() {
    return "login";
  }
}
