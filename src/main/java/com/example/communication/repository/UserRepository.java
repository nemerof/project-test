package com.example.communication.repository;

import com.example.communication.model.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  User findByUsername(String username);

  User findByActivationCode(String code);

  List<User> findAllByUsername(String username);
}
