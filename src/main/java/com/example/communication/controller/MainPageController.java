package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.service.MessageService;
import java.io.IOException;
import java.util.List;
import java.util.Set;
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

  public MainPageController(MessageService messageService) {
    this.messageService = messageService;
  }

  @GetMapping("/users")
  public String mainPage(Model model){
    return "users";
  }

  @GetMapping("/")
  public String main(
      @AuthenticationPrincipal User user,
      @RequestParam(required = false, defaultValue = "") String filter,
      Model model
  ) {
    List<MessageDTO> messages = messageService.getAllMessages(filter, user);
    model.addAttribute("loginUserId", user.getId());
    model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
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
      @PathVariable(value="id") Long id
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
        .forEach(redirectAttributes::addAttribute);

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
