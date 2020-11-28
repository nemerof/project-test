package com.example.communication.controller;

import com.example.communication.model.Message;
import com.example.communication.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class ControllerUtils {
    @Value("${upload.path}")
    private String getUploadPath;

    private static String uploadPathStatic;

    @Value("${upload.path}")
    public void setNameStatic(String name){
        ControllerUtils.uploadPathStatic = name;
    }

    private static MessageRepository messageRepository;

    @Autowired
    public ControllerUtils(MessageRepository repository) {
        messageRepository = repository;
    }

    public static void deleteMessage(Long id) {
        Message message = messageRepository.getOne(id);
        messageRepository.deleteById(id);
        File file = new File(uploadPathStatic + "/" + message.getFilename());
        file.delete();
    }

    public static void savePhoto(MultipartFile file, Message message) throws IOException {
        if (file != null && !file.getOriginalFilename().isEmpty()) {
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
}
