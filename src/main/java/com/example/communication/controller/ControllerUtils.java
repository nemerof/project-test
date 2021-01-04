package com.example.communication.controller;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.Role;
import com.example.communication.model.User;
import com.example.communication.repository.CommentRepository;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ControllerUtils {

    private static String uploadPathStatic;

    @Value("${upload.path}")
    public void setNameStatic(String name){
        ControllerUtils.uploadPathStatic = name;
    }

    private static MessageRepository messageRepository;

    private static CommentRepository commentRepository;

    private static UserRepository userRepository;

    private static JdbcTemplate jdbcTemplate;

    @Autowired
    public ControllerUtils(MessageRepository messageRepo,
                           UserRepository userRepo,
                           CommentRepository commentRepo,
                           JdbcTemplate jdbcTemp) {
        messageRepository = messageRepo;
        userRepository = userRepo;
        commentRepository = commentRepo;
        jdbcTemplate = jdbcTemp;
    }

    public static boolean deleteMessage(Long id, User user) {
        Message message = messageRepository.findById(id).get();
        for (Comment comment : message.getComments())
            commentRepository.delete(comment);
        boolean b1 = user.getRoles().contains(Role.ADMIN);
        boolean b2 = message.getUser().getId().equals(user.getId());
        if (b1 || b2) {
            String repostDel = "DELETE FROM reposts WHERE message_id = ?;";
            System.out.println("Reposts dependencies deleted: " + jdbcTemplate.update(repostDel, id));

            String likesDel = "DELETE FROM message_likes WHERE message_id = ?;";
            System.out.println("Likes dependencies deleted: " + jdbcTemplate.update(likesDel, id));

            messageRepository.deleteById(id);
            new File(uploadPathStatic + "/" + message.getFilename()).delete();
            return true;
        }

        return false;
    }

    public static void saveComment(MultipartFile file, Comment comment) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        comment.setPostTime(LocalDateTime.parse(formattedDateTime, formatter));

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String resultFilename = fileSave(file);
            comment.setFilename(resultFilename);
        }

        commentRepository.save(comment);
    }

    public static void saveMessage(MultipartFile file, Message message) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String formattedDateTime = LocalDateTime.now().format(formatter);
        message.setPostTime(LocalDateTime.parse(formattedDateTime, formatter));

        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String resultFilename = fileSave(file);
            message.setFilename(resultFilename);
        }

        messageRepository.save(message);
    }

    public static void saveMessage(MultipartFile file, User user) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            String resultFilename = fileSave(file);
            user.setProfilePic(resultFilename);
        } else {
            user.setProfilePic("default-profile-icon.png");
        }

        userRepository.save(user);
    }

    private static String fileSave(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPathStatic);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String uuidFile = UUID.randomUUID().toString();
        String resultFilename = uuidFile + "." + file.getOriginalFilename();

        file.transferTo(new File(uploadPathStatic + "/" + resultFilename));

        return resultFilename;
    }
}