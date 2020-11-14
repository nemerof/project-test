package com.example.communication.repository;

import com.example.communication.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message, Long> {

  Message findByText(String text);

  Iterable<Message> findByTextContains(String text);
}
