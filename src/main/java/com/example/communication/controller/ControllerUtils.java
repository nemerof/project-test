package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    private static UserRepository userRepository;

    @Autowired
    public ControllerUtils(
        MessageRepository messageRepo,
        UserRepository userRepo)
    {
        messageRepository = messageRepo;
        userRepository = userRepo;
    }

    public static void deleteMessage(Long id) {
        Message message = messageRepository.getOne(id);
        messageRepository.deleteById(id);
        new File(uploadPathStatic + "/" + message.getFilename()).delete();
    }

    public static void savePhoto(MultipartFile file, Message message) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPathStatic);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPathStatic + "/" + resultFilename));

            message.setFilename(resultFilename);
        }

        messageRepository.save(message);
    }

    //Do something
    public static void savePhoto(MultipartFile file, User user) throws IOException {
        if (file != null && !Objects.requireNonNull(file.getOriginalFilename()).isEmpty()) {
            File uploadDir = new File(uploadPathStatic);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFilename = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPathStatic + "/" + resultFilename));

            user.setProfilePic(resultFilename);
        } else {
            user.setProfilePic("default-profile-icon.png");
        }

        userRepository.save(user);
    }
}
