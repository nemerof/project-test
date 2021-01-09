package com.example.communication.controller.chat;

import com.example.communication.model.User;
import com.example.communication.model.chat.ChatMessage;
import com.example.communication.model.chat.ChatRoom;
import com.example.communication.model.chat.OutputMessage;
import com.example.communication.repository.ChatMessageRepository;
import com.example.communication.repository.ChatRoomRepository;
import com.example.communication.service.UserService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
public class ChatController {
    private final ChatRoomRepository chatRoomRepository;

    private final ChatMessageRepository chatMessageRepository;

    private final UserService userService;

    private final SimpMessagingTemplate messagingTemplate;

    public ChatController(ChatRoomRepository chatRoomRepository,
                          ChatMessageRepository chatMessageRepository,
                          UserService userService,
                          SimpMessagingTemplate messagingTemplate) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.userService = userService;
        this.messagingTemplate = messagingTemplate;
    }

    @GetMapping("/chat")
    public String chat(@AuthenticationPrincipal User currentUser,
                       @RequestParam(required = false) String userFilter,
                       @RequestParam(required = false, defaultValue = "none") String username,
                       Model model,
                       @Qualifier("usr")
                       @PageableDefault(size = 5, sort = {"id"}, direction = Sort.Direction.ASC)
                               Pageable pageableForUser,
                       @Qualifier("msg")
                       @PageableDefault(size = 15, sort = {"id"}, direction = Sort.Direction.DESC)
                               Pageable pageableForMessages) {

        Page<User> allUsers = userService.getAllUsers(currentUser.getUsername(), userFilter, pageableForUser);
        Page<OutputMessage> chatPage =
                chatMessageRepository.findAllByFromUAndToU(currentUser.getUsername(), username, pageableForMessages);

        List<OutputMessage> chatMessages = new ArrayList<>();
        List<OutputMessage> om = chatPage.getContent();

        for (int i = om.size() - 1; i >= 0; i--)
            chatMessages.add(om.get(i));

        model.addAttribute("users", allUsers);
        model.addAttribute("chatWithUser", username);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("chatPage", chatPage);
        model.addAttribute("chatMessages", chatMessages);
        return "chat";
    }

    @MessageMapping("/chat/{chatRoom}")
    public void send(@Payload ChatMessage message,
                     @DestinationVariable String chatRoom,
                     Principal user,
                     @RequestParam(required = false, defaultValue = "none") String username) {
        String time = new SimpleDateFormat("HH:mm").format(new Date());
        String to = username.substring(username.lastIndexOf(':') + 2, username.length() - 2);

        OutputMessage outputMessage = new OutputMessage(message.getFrom(), to, message.getText(), time);
        chatMessageRepository.save(outputMessage);

        messagingTemplate.convertAndSendToUser(to, "/queue/messages/" + chatRoom, outputMessage);
        messagingTemplate.convertAndSendToUser(user.getName(), "/queue/messages/" + chatRoom, outputMessage);
    }

    @GetMapping("/chat/{firstUser}/{secondUser}")
    public ResponseEntity<Long> getChatId(@PathVariable String firstUser,
                                          @PathVariable String secondUser) {
        ChatRoom cr = chatRoomRepository.findByFirstOrSecond(firstUser, secondUser).orElse(null);

        if (cr == null)
            cr = chatRoomRepository.save(new ChatRoom(firstUser, secondUser));

        return new ResponseEntity<>(cr.getId(), HttpStatus.OK);
    }
}
