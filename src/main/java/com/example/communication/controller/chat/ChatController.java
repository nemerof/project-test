package com.example.communication.controller.chat;

import com.example.communication.model.User;
import com.example.communication.model.chat.ChatMessage;
import com.example.communication.model.chat.ChatRoom;
import com.example.communication.model.chat.OutputMessage;
import com.example.communication.repository.ChatRoomRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
public class ChatController {
    @Autowired
    private ChatRoomRepository chatRoomRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

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

    @MessageMapping("/chat/{chatRoom}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String chatRoom,
                     Principal user,
                     @RequestParam(required = false, defaultValue = "none") String username) throws Exception {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        OutputMessage outputMessage = new OutputMessage(message.getFrom(), message.getText(), time);

        String to = username.substring(username.lastIndexOf(':')+2, username.length()-2);

        messagingTemplate.convertAndSendToUser(to, "/queue/messages/" + chatRoom, outputMessage);
        messagingTemplate.convertAndSendToUser(user.getName(), "/queue/messages/" + chatRoom, outputMessage);
    }

    @GetMapping("/chat/{firstUser}/{secondUser}")
    public ResponseEntity<Long> getChatId(@PathVariable String firstUser,
                          @PathVariable String secondUser) {
        ChatRoom cr = chatRoomRepository.findByFirstOrSecond(firstUser, secondUser).orElse(null);

        if (cr == null)
            cr = chatRoomRepository.save(new ChatRoom(firstUser, secondUser));

        ResponseEntity<Long> entity = new ResponseEntity<>(cr.getId(), HttpStatus.OK);

        return entity;
    }
}
