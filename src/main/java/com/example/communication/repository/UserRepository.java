package com.example.communication.repository;

import com.example.communication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByUsername(String username);
}
