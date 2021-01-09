package com.example.communication.controller;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.Optional;

@Controller
public class CommentController {

    private final MessageRepository messageRepository;

    public CommentController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostMapping("/comment/{messageId}")
    public String comment(@RequestParam String text,
                          @RequestParam MultipartFile file,
                          @AuthenticationPrincipal User user,
                          @PathVariable Long messageId,
                          RedirectAttributes redirectAttributes,
                          @RequestHeader(required = false) String referer) throws IOException {
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
