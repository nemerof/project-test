package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;
import java.util.Set;

@Controller
public class MainPageController {

    private final MessageService messageService;

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    public MainPageController(MessageService messageService,
                              MessageRepository messageRepository,
                              UserRepository userRepository) {
        this.messageService = messageService;
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
        Message message = new Message(text, user);
        ControllerUtils.saveMessageEntity(file, message);
        model.addAttribute("filter", filter);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String delete(@AuthenticationPrincipal User user,
                         @PathVariable(value = "id") Long id,
                         RedirectAttributes redirectAttributes,
                         @RequestHeader(required = false) String referer) {
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
                .forEach(redirectAttributes::addAttribute);

        Optional<Message> option = messageRepository.findById(id);
        Message message;
        if (option.isPresent()) {
            message = option.get();
        } else {
            return "redirect:" + components.getPath();
        }
        boolean isMessageRepost = !user.getId().equals(message.getUser().getId()) && !user.getRoles().contains(Role.ADMIN);

        if (isMessageRepost) {
            try {
                user.getReposts().remove(message);
            } catch (Exception ignored) {
            }

            userRepository.save(user);

            return "redirect:/messages/" + id + "/dislike";
        } else {
            if (ControllerUtils.deleteMessage(id, user)) {
                System.out.println("Successfully deleted");
            } else {
                System.out.println("Problem during deleting");
            }
            return "redirect:" + components.getPath();
        }
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

    @GetMapping("/messages/{message}/dislike")
    public String dislike(@AuthenticationPrincipal User currentUser,
                          @PathVariable Message message,
                          RedirectAttributes redirectAttributes,
                          @RequestHeader(required = false) String referer) {
        Set<User> likes = message.getLikes();
        likes.remove(currentUser);

        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

        components.getQueryParams()
                .forEach(redirectAttributes::addAttribute);
        return "redirect:" + components.getPath();
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
