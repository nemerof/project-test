package com.example.communication.controller.chat;

import com.example.communication.model.User;
import com.example.communication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/chat")
public class ChatController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String chat(@AuthenticationPrincipal User currentUser,
                       @RequestParam(required = false) String userFilter,
                       @RequestParam(required = false) String username,
                       Model model,
                       @PageableDefault(size = 10, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<User> allUsers = userService.getAllUsers(currentUser.getUsername(), userFilter, pageable);
        model.addAttribute("users", allUsers);
        model.addAttribute("chatWuser", username);
        return "chat";
    }
}
