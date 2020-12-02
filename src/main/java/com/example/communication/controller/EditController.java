package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;

import java.io.File;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

  @Autowired
  private UserRepository userRepository;

  @Value("${upload.path}")
  private String uploadPath;

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
      @AuthenticationPrincipal User user1,
      @RequestParam String username,
      @RequestParam("profilePic") MultipartFile file
  ) throws IOException {
    User user = userRepository.findById(user1.getId()).get();
    user.setUsername(username);
    String profPic = user.getProfilePic();
    if (profPic != null && !profPic.equals("") && !profPic.equals("default-profile-icon.png")) {
      File file1 = new File(uploadPath + "/" + user.getProfilePic());
      file1.delete();
    }
    ControllerUtils.savePhoto(file, user);
    return "redirect:/profile/"+user.getId();
  }
}
