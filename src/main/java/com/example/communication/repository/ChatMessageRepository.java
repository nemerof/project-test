package com.example.communication.repository;

import com.example.communication.model.chat.OutputMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatMessageRepository extends JpaRepository<OutputMessage, Long> {

    @Query("select distinct om from OutputMessage om " +
            "where (om.toU = :from and om.fromU = :to) " +
            "or (om.fromU = :from and om.toU = :to) ")
    Page<OutputMessage> findAllByFromUAndToU(String from, String to, Pageable pageable);
}
