package com.example.communication.controller;

import com.example.communication.model.User;
import com.example.communication.service.CommentService;
import java.io.IOException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/comment/{messageId}")
    public String comment(@RequestParam String text,
                          @RequestParam MultipartFile file,
                          @AuthenticationPrincipal User user,
                          @PathVariable Long messageId,
                          RedirectAttributes redirectAttributes,
                          @RequestHeader(required = false) String referer) throws IOException {

        return commentService.comment(text, file, user, messageId, redirectAttributes, referer);
    }
}
