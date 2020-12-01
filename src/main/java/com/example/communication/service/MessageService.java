package com.example.communication.service;

import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final MessageRepository messageRepository;

    private final EntityManager em;

    public MessageService(MessageRepository messageRepository,
        EntityManager em) {
        this.messageRepository = messageRepository;
        this.em = em;
    }

    public List<MessageDTO> getAllMessages(String filter, User user) {
        List<MessageDTO> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = (List<MessageDTO>) messageRepository.findByTextContains(filter, user);
        } else {
            messages = messageRepository.findAll(user);
        }
        Collections.reverse(messages);

        return messages;
    }
}
