package com.example.communication.controller;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

  private final UserRepository userRepository;

  private final MessageRepository messageRepository;

  private final UserService service;

  private final JdbcTemplate jdbcTemplate;

  public UserController(UserRepository repository,
                        MessageRepository messageRepository,
                        UserService service,
                        JdbcTemplate jdbcTemplate) {
    this.userRepository = repository;
    this.messageRepository = messageRepository;
    this.service = service;
    this.jdbcTemplate = jdbcTemplate;
  }

  @GetMapping
  public String userList(
      @RequestParam(required = false, defaultValue = "") String userFilter,
      Model model
  ) {
    List<User> users = service.getAllUsers(userFilter);
    model.addAttribute("users", users);
    return "userList";
  }

  @GetMapping("{user}")
  public String userEdit(
      @PathVariable User user,
      Model model
  ) {
    model.addAttribute("user", user);
    model.addAttribute("roles", Role.values());
    return "userEdit";
  }

  @GetMapping("/delete/{user}")
  public String userDelete(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user,
      @PageableDefault(sort = { "postTime" }, direction = Sort.Direction.ASC, size = 5) Pageable pageable
  ) {
//    for (MessageDTO mes : messageRepository.findByUserId(currentUser, user, pageable)) {
//      System.out.println("Actual reposts deleted: " + jdbcTemplate.update("DELETE FROM reposts WHERE message_id = ?;", mes.getId()));
//      System.out.println("Actual likes deleted: " + jdbcTemplate.update("DELETE FROM message_likes WHERE message_id = ?;", mes.getId()));
//    } todo

    String repostDel = "DELETE FROM reposts WHERE user_id = ?;";
    System.out.println("Reposts dependencies deleted: " + jdbcTemplate.update(repostDel, user.getId()));

    String likesDel = "DELETE FROM message_likes WHERE user_id = ?;";
    System.out.println("Likes dependencies deleted: " + jdbcTemplate.update(likesDel, user.getId()));


//    for (MessageDTO mes : messageRepository.findByUserId(currentUser, user, pageable)) {
//      ControllerUtils.deleteMessage(mes.getId(), currentUser);
//    } todo

    user.setReposts(new HashSet<>());
    userRepository.save(user);
    userRepository.delete(user);
    return "redirect:/user";
  }

  @PostMapping
  public String userSave(
      @RequestParam String username,
      @RequestParam Map<String, String> form,
      @RequestParam("userId") User user
  ) {
    user.setUsername(username);

    Set<String> roles = Arrays.stream(Role.values())
        .map(Role::name)
        .collect(Collectors.toSet());

    user.getRoles().clear();

    for (String key : form.keySet()) {
      if (roles.contains(key)) {
        user.getRoles().add(Role.valueOf(key));
      }
    }

    userRepository.save(user);
    return "redirect:/user";
  }
}
