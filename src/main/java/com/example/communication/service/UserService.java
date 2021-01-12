package com.example.communication.service;

import com.example.communication.controller.ControllerUtils;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.model.dto.MessageDTO;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {


    private final UserRepository userRepository;

    private final MessageRepository messageRepository;

    private final JdbcTemplate jdbcTemplate;

    public UserService(UserRepository repository,
        MessageRepository messageRepository,
        JdbcTemplate jdbcTemplate) {
        this.userRepository = repository;
        this.messageRepository = messageRepository;
        this.jdbcTemplate = jdbcTemplate;
    }


    public Page<User> getAllUsers(String user, String filter, Pageable pageable) {
        Page<User> users;
        if (filter != null && !filter.isEmpty()) {
            users = userRepository.findAllByUsername(filter, user, pageable);
        } else {
            users = userRepository.findAll(user, pageable);
        }

        return users;
    }

    public List<User> getAllUsersTest() {
        return userRepository.findAllTest();
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        UserDetails details = userRepository.findByUsername(s);
        if (details == null)
            details = new User();
        return details;
    }

    public void editUserInUserList(User user, Map<String, String> form) {
        Set<String> roles = Arrays.stream(Role.values())
            .map(Role::name)
            .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepository.save(user);
    }

    public void deleteUserInUserList(User user, User currentUser, Pageable pageable) {
        List<Long> reposts = jdbcTemplate.query("select * from reposts where user_id = ?",
            new Object[]{user.getId()}, (resultSet, i) -> (long) resultSet.getInt("message_id"));

        String repostDel = "DELETE FROM reposts WHERE user_id = ?;";
        System.out.println("Reposts dependencies deleted: " + jdbcTemplate.update(repostDel, user.getId()));

        String likesDel = "DELETE FROM message_likes WHERE user_id = ?;";
        System.out.println("Likes dependencies deleted: " + jdbcTemplate.update(likesDel, user.getId()));


        for (MessageDTO mes : messageRepository.findByUserId(currentUser, user, pageable, reposts)) {
            if (ControllerUtils.deleteMessage(mes.getId(), currentUser)) {
                System.out.println("Successfully deleted");
            } else {
                System.out.println("Problem during deleting");
            }
        }

        user.setReposts(new HashSet<>());
        userRepository.save(user);
        userRepository.delete(user);
    }
}
