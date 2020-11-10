package com.example.communication.repository;

import com.example.communication.model.CommunicationUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<CommunicationUser, Integer> {

}
