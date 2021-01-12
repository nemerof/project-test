package com.example.communication.controller;

import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.ProfileService;
import com.example.communication.service.UserService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
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
public class ProfileController {

    private final MessageRepository messageRepository;

    private final UserRepository userRepository;

    private final UserService userService;

    private final ProfileService profileService;

    private final JdbcTemplate jdbcTemplate;

    private final Connection conn;

    public ProfileController(MessageRepository messageRepository,
        UserRepository userRepository,
        UserService userService,
        ProfileService profileService, JdbcTemplate jdbcTemplate,
        Connection connection) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.profileService = profileService;
        this.jdbcTemplate = jdbcTemplate;
        this.conn = connection;
    }

    @GetMapping("/profile/{id}")
    public String profile(@AuthenticationPrincipal User currentUser,
                          @PathVariable(value = "id") Long id,
                          Model model,
                          @PageableDefault(sort = {"postTime"}, direction = Sort.Direction.ASC, size = 5) Pageable pageable) {
        Optional<User> optionalUser = userRepository.findById(id);
        User user;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
        } else {
            return "redirect:/";
        }
        List<Long> reposts = jdbcTemplate.query(
                "select * from reposts where user_id = ?",
                new Object[]{user.getId()},
                (resultSet, i) -> (long) resultSet.getInt("message_id"));

        Page<MessageDTO> userMessages =
                messageRepository.findByUserId(currentUser, user, pageable, reposts);

        model.addAttribute("url", "/profile/" + user.getId());
        model.addAttribute("currentUser", user);
        model.addAttribute("profileName", user.getUsername());
        model.addAttribute("messages", userMessages);
        model.addAttribute("subscribers", user.getSubscribers().size());
        model.addAttribute("subscriptions", user.getSubscriptions().size());
        model.addAttribute("profileId", user.getId());
        model.addAttribute("curProfPic", user.getProfilePic());
        model.addAttribute("isSubscriber", user.getSubscribers().contains(currentUser));
        model.addAttribute("isCurrentUser", currentUser.equals(user));
        model.addAttribute("isAdmin", user.getRoles().contains(Role.ADMIN));
        model.addAttribute("formatDateTime", new FormatDateTimeMethodModel());
        return "profile";
    }

    @PostMapping("/profile/{id}")
    public String addMessage(@RequestParam("file") MultipartFile file,
                      @PathVariable(value = "id") Long id,
                      @AuthenticationPrincipal User user,
                      @RequestParam String text, Model model
    ) throws IOException {
        profileService.addMessage(user, text, file);
        model.addAttribute("filter", "");
        return "redirect:/profile/" + id;
    }

    @GetMapping("profile/subscribers/{id}")
    public String getSubscribers(@PathVariable(value = "id") Long id,
                                 Model model) {

        User user = profileService.getUserById(id);
        if (user == null) return "redirect:/";
        model.addAttribute("subscribers", user.getSubscribers());
        return "subscribers";
    }

    @GetMapping("profile/subscriptions/{id}")
    public String getSubscriptions(@PathVariable(value = "id") Long id,
                                   Model model) {
        User user = profileService.getUserById(id);
        if (user == null) return "redirect:/";
        model.addAttribute("subscriptions", user.getSubscriptions());
        return "subscriptions";
    }

    @GetMapping("profile/subscribe/{id}")
    public String subscribe(@PathVariable(value = "id") Long id,
                            @AuthenticationPrincipal User currentUser) {

        if(!profileService.subscribe(id, currentUser)) return "redirect:/";
        return "redirect:/profile/" + id;
    }

    @GetMapping("profile/unsubscribe/{id}")
    public String unsubscribe(@PathVariable(value = "id") Long id,
                              @AuthenticationPrincipal User currentUser) {

        if(!profileService.unsubscribe(id, currentUser)) return "redirect:/";
        return "redirect:/profile/" + id;
    }

    @GetMapping("/repost/{messageId}")
    public String repost(@PathVariable Long messageId,
                         @AuthenticationPrincipal User currentUser,
                         RedirectAttributes redirectAttributes,
                         @RequestHeader(required = false) String referer) throws SQLException {
        UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
        components.getQueryParams()
            .forEach(redirectAttributes::addAttribute);

        if(!profileService.repost(messageId, currentUser)) return "redirect:" + components.getPath();
        return "redirect:/messages/" + messageId + "/like";
    }
}