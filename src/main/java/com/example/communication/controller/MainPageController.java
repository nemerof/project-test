package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.MainService;
import com.example.communication.service.MessageService;
import java.io.IOException;
import java.util.Set;
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
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MainPageController {

    private final MessageService messageService;

    private final MainService mainService;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MainPageController(MessageService messageService,
        MainService mainService,
        MessageRepository messageRepository,
        UserRepository userRepository) {
        this.messageService = messageService;
        this.mainService = mainService;
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public String mainPage() {
        return "users";
    }

    @GetMapping("/")
    public String main(@AuthenticationPrincipal User user,
                       @RequestParam(required = false, defaultValue = "") String filter,
                       Model model,
                       @PageableDefault(sort = {"id"}, direction = Sort.Direction.DESC, size = 5) Pageable pageable) {
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
    public String add(@RequestParam(required = false, defaultValue = "") String filter,
                      @AuthenticationPrincipal User user,
                      @RequestParam String text, Model model,
                      @RequestParam("file") MultipartFile file) throws IOException {
        mainService.addMessage(user, text, file);
        model.addAttribute("filter", filter);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable(value = "id") Long id,
                         RedirectAttributes redirectAttributes,
                         @RequestHeader(required = false) String referer) {

        return mainService.delete(user, id, redirectAttributes, referer);
    }

    @GetMapping("/messages/{message}/like")
    public String like(
            @AuthenticationPrincipal User currentUser,
            @PathVariable Message message,
            RedirectAttributes redirectAttributes,
            @RequestHeader(required = false) String referer) {

        return mainService.like(currentUser, message, redirectAttributes, referer);
    }

    @GetMapping("/messages/{message}/dislike")
    public String dislike(@AuthenticationPrincipal User currentUser,
                          @PathVariable Message message,
                          RedirectAttributes redirectAttributes,
                          @RequestHeader(required = false) String referer) {


        return mainService.dislike(currentUser, message ,redirectAttributes, referer);
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
