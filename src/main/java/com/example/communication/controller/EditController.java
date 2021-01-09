package com.example.communication.controller;

import com.example.communication.model.User;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Controller
@RequestMapping("/edit")
public class EditController {

    private final Storage storage;

    @Value("${bucket.name}")
    private String bucketName;

    public EditController(Storage storage) {
        this.storage = storage;
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

        user.setUsername(username == null ? user.getUsername() : username);
        user.setRealName(realName == null ? user.getRealName() : realName);
        user.setDateOfBirth(dateOfBirth == null ? user.getDateOfBirth() : dateOfBirth);
        user.setCity(city == null ? user.getCity() : city);

        String profPic = user.getProfilePic();
        if (!Objects.requireNonNull(file.getOriginalFilename()).equals("") && profPic != null && !profPic.equals("") && !profPic.equals("default-profile-icon.png")) {
            BlobId blobId = BlobId.of(bucketName, user.getProfilePic());
            storage.delete(blobId);
        }
        if (!file.getOriginalFilename().equals("")) {
            ControllerUtils.saveMessage(file, user);
        }
        return "redirect:/profile/" + user.getId();
    }
}
