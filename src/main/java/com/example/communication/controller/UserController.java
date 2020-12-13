package com.example.communication.controller;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/user")
//@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

  private final UserRepository userRepository;

  private final MessageRepository messageRepository;

  private final UserService service;

  public UserController(UserRepository repository,
      MessageRepository messageRepository, UserService service) {
    this.userRepository = repository;
    this.messageRepository = messageRepository;
    this.service = service;
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

  @GetMapping("delete/{user}")
  public String userDelete(
      @AuthenticationPrincipal User currentUser,
      @PathVariable User user
  ) {
    for(MessageDTO mes : messageRepository.findByUserId(currentUser, user)){
      ControllerUtils.deleteMessage(mes.getId(), currentUser);
    };
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
