package com.example.communication.controller;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.service.UserService;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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

    private final UserService service;

    public UserController(UserService service) {
        this.service = service;
    }


    @GetMapping
    public String userList(@AuthenticationPrincipal User currentUser,
                           @RequestParam(required = false, defaultValue = "") String userFilter,
                           @PageableDefault(sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable,
                           Model model) {
        Page<User> users = service.getAllUsers(currentUser.getUsername(), userFilter, pageable);
        model.addAttribute("users", users);
        return "userList";
    }

    @GetMapping("{user}")
    public String userEdit(@PathVariable User user,
                           Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEdit";
    }

    @GetMapping("/delete/{user}")
    public String userDeleteInUserList(@AuthenticationPrincipal User currentUser,
                             @PathVariable User user,
                             @PageableDefault(sort = {"postTime"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable) {

        service.deleteUserInUserList(user, currentUser, pageable);
        return "redirect:/user";
    }

    @PostMapping
    public String userEditInUserList(@RequestParam Map<String, String> form,
                           @RequestParam("userId") User user) {

        service.editUserInUserList(user, form);
        return "redirect:/user";
    }
}
