package com.example.communication.controller.chat;

import com.example.communication.model.User;
import com.example.communication.model.chat.Message;
import com.example.communication.model.chat.OutputMessage;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/chat")
    public String chat(@AuthenticationPrincipal User currentUser,
                       @RequestParam(required = false) String userFilter,
                       @RequestParam(required = false, defaultValue = "none") String username,
                       Model model,
                       @PageableDefault(size = 10, sort = {"id"}, direction = Sort.Direction.ASC) Pageable pageable) {
        User user = userRepository.findById(currentUser.getId()).get();
        Page<User> allUsers = userService.getAllUsers(currentUser.getUsername(), userFilter, pageable);
        model.addAttribute("users", allUsers);
        model.addAttribute("chatWuser", username);
        model.addAttribute("currentUser", user);
        return "chat";
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public OutputMessage send(Message message) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        return new OutputMessage(message.getFrom(), message.getText(), time);
    }
}
