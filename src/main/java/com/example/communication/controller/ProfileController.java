package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
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

  private final JdbcTemplate jdbcTemplate;

  private final Connection conn;

  public ProfileController(MessageRepository messageRepository,
                           UserRepository userRepository,
                           UserService userService,
                           JdbcTemplate jdbcTemplate,
                           Connection connection) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
    this.userService = userService;
    this.jdbcTemplate = jdbcTemplate;
    this.conn = connection;
  }

  @GetMapping("/profile/{id}")
  public String profile(
          @AuthenticationPrincipal User currentUser,
          @PathVariable(value="id") Long id,
          Model model,
          @PageableDefault(sort = { "postTime" }, direction = Sort.Direction.ASC, size = 5) Pageable pageable)  {
    User user = userRepository.findById(id).get();
    List<Long> reposts = new ArrayList<>();

    reposts = jdbcTemplate.query("select * from reposts where user_id = ?", new Object[]{user.getId()}, new RowMapper<Long>() {
      @Override
      public Long mapRow(ResultSet resultSet, int i) throws SQLException {
        return (long) resultSet.getInt("message_id");
      }
    });

    Page<MessageDTO> userMessages =
            messageRepository.findByUserId(currentUser, user, pageable, reposts);

//    for (Message repost : user.getReposts()) {
//      final boolean[] isLiked = {false};
//
//      //Statement st = conn.createStatement();
//      //ResultSet resultSet = st.executeQuery("select * from message_likes");
//      jdbcTemplate.query("select * from message_likes", (RowMapper<Object>) (resultSet, i) -> {
//        int messageId = resultSet.getInt("message_id");
//        int userId = resultSet.getInt("user_id");
//        if (messageId == repost.getId() && userId == user.getId())
//          isLiked[0] = true;
//        return "skip";
//      });
//
//      userMessages.getContent().add(new MessageDTO(repost, (long) repost.getLikes().size(), isLiked[0]));
//    }
//    userMessages.getContent().sort(new Comparator<MessageDTO>() {
//      @Override
//      public int compare(MessageDTO o1, MessageDTO o2) {
//        return o1.getPostTime().compareTo(o2.getPostTime());
//      }
//    });
    model.addAttribute("url", "/profile/" + user.getId());
    model.addAttribute("user", user);
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
  public String add(
          @RequestParam("file") MultipartFile file,
          @PathVariable(value="id") Long id,
          @AuthenticationPrincipal User user,
          @RequestParam String text, Model model
  ) throws IOException {
    Message message = new Message(text, user);
    ControllerUtils.saveMessage(file, message);
    model.addAttribute("filter", "");
    return "redirect:/profile/"+id;
  }

  @GetMapping("profile/subscribers/{id}")
  public String getSubscribers(@PathVariable(value = "id") Long id,
                               Model model) {
    User user = userRepository.findById(id).get();
    model.addAttribute("subscribers", user.getSubscribers());
    return "subscribers";
  }

  @GetMapping("profile/subscriptions/{id}")
  public String getSubscriptions(@PathVariable(value = "id") Long id,
                                 Model model) {
    User user = userRepository.findById(id).get();
    model.addAttribute("subscriptions", user.getSubscriptions());
    return "subscriptions";
  }

  @GetMapping("profile/subscribe/{id}")
  public String subscribe(@PathVariable(value = "id") Long id,
                          @AuthenticationPrincipal User currentUser) {
    User user = userRepository.findById(id).get();
    userService.subscribe(currentUser, user);
    return "redirect:/profile/"+id;
  }

  @GetMapping("profile/unsubscribe/{id}")
  public String unsubscribe(@PathVariable(value = "id") Long id,
                            @AuthenticationPrincipal User currentUser) {
    User user = userRepository.findById(id).get();
    userService.unsubscribe(currentUser, user);
    return "redirect:/profile/"+id;
  }

  @GetMapping("/repost/{messageId}")
  public String repost(@PathVariable Long messageId,
                       @AuthenticationPrincipal User currentUser,
                       RedirectAttributes redirectAttributes,
                       @RequestHeader(required = false) String referer) throws SQLException {
    currentUser.getReposts().add(messageRepository.findById(messageId).get());

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

    components.getQueryParams()
            .forEach(redirectAttributes::addAttribute);

    try {
      userRepository.save(currentUser);
    } catch (Exception e) {
      System.err.println("U can repost only one time");

      return "redirect:" + components.getPath();
    }

    Statement st = conn.createStatement();
    ResultSet resultSet = st.executeQuery("select * from message_likes");

    while (resultSet.next()) {
      int messageTableId = resultSet.getInt("message_id");
      int userTableId = resultSet.getInt("user_id");
      if (messageId == messageTableId && userTableId == currentUser.getId())
        return "redirect:" + components.getPath();
    }

    return "redirect:/messages/"+messageId+"/like";
  }
}