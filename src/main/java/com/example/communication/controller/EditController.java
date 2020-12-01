package com.example.communication.controller;

import com.example.communication.model.User;
import java.io.IOException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/edit")
public class EditController {

  @GetMapping
  public String edit(
      @AuthenticationPrincipal User user, Model model)
  {
    model.addAttribute("username", user.getUsername());
    model.addAttribute("userId", user.getId());
    model.addAttribute("profilePic", user.getProfilePic());
    return "editUser";
  }

  @PostMapping
  public String edit(
      @AuthenticationPrincipal User user,
      @RequestParam String username,
      @RequestParam("profilePic") MultipartFile file
  ) throws IOException {
    user.setUsername(username);
    ControllerUtils.savePhoto(file, user);
    return "redirect:/profile/"+user.getId();
  }
}
