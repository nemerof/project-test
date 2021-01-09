package com.example.communication.repository;

import com.example.communication.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByActivationCode(String code);

    @Query("select distinct u from User u where u.username like %:username% and u.username <> :currentUser")
    Page<User> findAllByUsername(@Param("username") String username, @Param("currentUser") String currentUser, Pageable pageable);

    @Query("select distinct u from User u where u.username <> :currentUser")
    Page<User> findAll(@Param("currentUser") String currentUser, Pageable pageable);
}
