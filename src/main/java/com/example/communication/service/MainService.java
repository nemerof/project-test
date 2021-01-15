package com.example.communication.service;

import com.example.communication.controller.ControllerUtils;
import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class MainService {

  private final MessageRepository messageRepository;

  private final UserRepository userRepository;

  public MainService(MessageRepository messageRepository,
      UserRepository userRepository) {
    this.messageRepository = messageRepository;
    this.userRepository = userRepository;
  }

  public void addMessage(User user, String text, MultipartFile file) throws IOException {
    Message message = new Message(text, user);
    ControllerUtils.saveMessageEntity(file, message);
  }

  public String delete(User user, Long id,
      RedirectAttributes redirectAttributes, String referer) {
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
    boolean isMessageRepost = !user.getId().equals(message.getUser().getId()) && !user.getRoles().contains(
        Role.ADMIN);

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

  public String like(User currentUser, Message message,
      RedirectAttributes redirectAttributes, String referer) {
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

  public String dislike(User currentUser, Message message,
      RedirectAttributes redirectAttributes, String referer) {

    Set<User> likes = message.getLikes();
    likes.remove(currentUser);

    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();

    components.getQueryParams()
        .forEach(redirectAttributes::addAttribute);
    return "redirect:" + components.getPath();
  }
}
