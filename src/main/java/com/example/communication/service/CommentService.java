package com.example.communication.service;

import com.example.communication.controller.ControllerUtils;
import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import java.io.IOException;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class CommentService {

  private final MessageRepository messageRepository;

  public CommentService(MessageRepository messageRepository) {
    this.messageRepository = messageRepository;
  }

  public String comment(String text, MultipartFile file, User user,
      Long messageId, RedirectAttributes redirectAttributes, String referer) throws IOException {
    UriComponents components = UriComponentsBuilder.fromHttpUrl(referer).build();
    components.getQueryParams()
        .forEach(redirectAttributes::addAttribute);
    Optional<Message> optMessage = messageRepository.findById(messageId);
    Message message;
    if (optMessage.isPresent()) {
      message = optMessage.get();
    } else {
      return "redirect:" + components.getPath();
    }

    Comment comment = new Comment(text, user, message);
    ControllerUtils.saveMessageEntity(file, comment);

    return "redirect:" + components.getPath();
  }
}
