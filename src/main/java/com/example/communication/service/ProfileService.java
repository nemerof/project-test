package com.example.communication.service;

import com.example.communication.controller.ControllerUtils;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class ProfileService {

  private final UserRepository userRepository;

  private final MessageRepository messageRepository;

  private final Connection conn;

  public ProfileService(UserRepository userRepository,
      MessageRepository messageRepository, Connection conn) {
    this.userRepository = userRepository;
    this.messageRepository = messageRepository;
    this.conn = conn;
  }

  public void addMessage(User user, String text, MultipartFile file) throws IOException {
    Message message = new Message(text, user);
    ControllerUtils.saveMessageEntity(file, message);
  }

  public User getUserById(Long id) {
    Optional<User> optionalUser = userRepository.findById(id);
    return optionalUser.orElse(null);
  }

  public boolean subscribe(Long id, User currentUser) {
    Optional<User> optionalUser = userRepository.findById(id);
    User user = optionalUser.orElse(null);
    if (user == null) return false;

    user.getSubscribers().add(currentUser);
    userRepository.save(user);
    return true;
  }

  public boolean unsubscribe(Long id, User currentUser) {
    Optional<User> optionalUser = userRepository.findById(id);
    User user = optionalUser.orElse(null);
    if (user == null) return false;

    user.getSubscribers().remove(currentUser);
    userRepository.save(user);
    return true;
  }

  public String repost(Long messageId, User currentUser, RedirectAttributes redirectAttributes,
      String referer) throws SQLException {

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
    components.getQueryParams()
        .forEach(redirectAttributes::addAttribute);

    Optional<Message> optionalMessage = messageRepository.findById(messageId);
    Message message;
    if (optionalMessage.isPresent()) {
      message = optionalMessage.get();
    } else {
      return "redirect:" + components.getPath();
    }
    currentUser.getReposts().add(message);

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

    return "redirect:/messages/" + messageId + "/like";
  }
}
