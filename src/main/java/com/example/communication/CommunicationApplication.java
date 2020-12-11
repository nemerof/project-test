package com.example.communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunicationApplication {
  private static final String classPathFirst = System.getProperty("java.class.path");
  private static final String realClassPath = classPathFirst.substring(0, classPathFirst.indexOf("target"))
                        + "src\\main\\resources\\static\\images\\default-profile-icon.png";
  private static final String uploadPath = "/D:/communication";

  public static void main(String[] args) throws IOException {
    File defaultPhotoFile = new File(realClassPath);
    if (!defaultPhotoFile.exists()) {
      try (
              InputStream from = new FileInputStream(defaultPhotoFile);
              OutputStream to = new FileOutputStream(new File(uploadPath + "/default-profile-icon.png"))) {
        byte[] buffer = new byte[1024];
        int length;
        while ((length = from.read(buffer)) > 0) {
          to.write(buffer, 0, length);
        }
      }
    }
    SpringApplication.run(CommunicationApplication.class, args);
  }

}
