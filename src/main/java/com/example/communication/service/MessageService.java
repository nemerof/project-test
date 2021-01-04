package com.example.communication.service;

import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import java.util.Collections;
import java.util.List;
import java.util.Set;
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

    public Page<MessageDTO> getMainPageMessages(String filter, User user, Set<User> users, Pageable pageable) {
        users.add(user);
        Page<MessageDTO> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = messageRepository.findByTextContains(filter, user, users, pageable);
        } else {
            messages = messageRepository.findAll(user, users, pageable);
        }

        return messages;
    }

    //for testing
    public List<MessageDTO> getAllMessagesTest(String filter, User user) {
        List<MessageDTO> messages;
        if (filter != null && !filter.isEmpty()) {
            messages = (List<MessageDTO>) messageRepository.findByTextContainsTest(filter, user);
        } else {
            messages = messageRepository.findAllTest(user);
        }
        Collections.reverse(messages);

        return messages;
    }
}
