package com.example.communication.service;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

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
