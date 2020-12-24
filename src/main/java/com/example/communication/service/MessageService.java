package com.example.communication.service;

import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public Page<MessageDTO> getAllMessages(String filter, User user, Pageable pageable) {
        Page<MessageDTO> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = (Page<MessageDTO>) messageRepository.findByTextContains(filter, user, pageable);
        } else {
            messages = messageRepository.findAll(user, pageable);
        }

        return messages;
    }
}
