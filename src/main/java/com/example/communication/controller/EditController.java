package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.service.EditService;
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

    private final EditService editService;

    public EditController(EditService editService) {
        this.editService = editService;
    }

    @GetMapping
    public String edit(@AuthenticationPrincipal User user, Model model) {
        model.addAttribute("birthDate", user.getDateOfBirth());
        model.addAttribute("realName", user.getRealName());
        model.addAttribute("userId", user.getId());
        model.addAttribute("city", user.getCity());
        return "editUser";
    }

    @PostMapping
    public String edit(@AuthenticationPrincipal User user,
                       @RequestParam(required = false) String username,
                       @RequestParam(required = false) String realName,
                       @RequestParam(required = false) String dateOfBirth,
                       @RequestParam(required = false) String city,
                       @RequestParam(required = false, name = "profilePic") MultipartFile file) throws IOException {

        editService.edit(user, username, realName, dateOfBirth, city, file);
        return "redirect:/profile/" + user.getId();
    }
}
