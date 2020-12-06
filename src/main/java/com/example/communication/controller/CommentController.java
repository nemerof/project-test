package com.example.communication.controller;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.CommentRepository;
import com.example.communication.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;


import java.io.IOException;

@Controller
public class CommentController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/comment/{messageId}")
    public String comment(@RequestParam String text,
                          @RequestParam MultipartFile file,
                          Model model,
                          @AuthenticationPrincipal User user,
                          @PathVariable Long messageId) throws IOException {
        Message message = messageRepository.findById(messageId).get();
        Comment comment = new Comment(text, user, message);
        ControllerUtils.saveComment(file, comment);


        return "redirect:/profile/"+user.getId();
    }
}
