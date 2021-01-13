package com.example.communication.service;

import com.example.communication.controller.ControllerUtils;
import com.example.communication.model.User;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import java.io.IOException;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class EditService {

  private final Storage storage;

  @Value("${bucket.name}")
  private String bucketName;

  public EditService(Storage storage) {
    this.storage = storage;
  }

  public void edit(User user, String username, String realName,
      String dateOfBirth, String city, MultipartFile file) throws IOException {

    user.setUsername(username == null ? user.getUsername() : username);
    user.setRealName(realName == null ? user.getRealName() : realName);
    user.setDateOfBirth(dateOfBirth == null ? user.getDateOfBirth() : dateOfBirth);
    user.setCity(city == null ? user.getCity() : city);

    String profPic = user.getProfilePic();
    if (!Objects.requireNonNull(file.getOriginalFilename()).equals("") && profPic != null && !profPic.equals("") && !profPic.equals("default-profile-icon.png")) {
      BlobId blobId = BlobId.of(bucketName, user.getProfilePic());
      storage.delete(blobId);
    }
    if (!file.getOriginalFilename().equals("")) {
      ControllerUtils.saveMessage(file, user);
    }
  }
}
