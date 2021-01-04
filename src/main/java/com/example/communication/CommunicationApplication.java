package com.example.communication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CommunicationApplication {
  private static final String classPathFirst = System.getProperty("java.class.path");
  private static final String realClassPath =
      "/home/friday58/IdeaProjects/communication/src/main/resources/static/images/default-profile-icon.png";
  private static final String uploadPath = "/home/friday58/IdeaProjects/communication";

  public static void main(String[] args) throws IOException {
    File defaultPhotoFile = new File(realClassPath);
    if (!new File(uploadPath + "/default-profile-icon.png").exists()) {
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

//@SpringBootApplication
//public class CommunicationApplication {
//
//  public static void main(String[] args) throws IOException {
//    try (
//        InputStream from = new FileInputStream(
//            new File("/home/friday58/IdeaProjects/communication/src/main/resources/static/images/default-profile-icon.png"));
//        OutputStream to = new FileOutputStream(new File("/home/friday58/Documents/upload_dir/default-profile-icon.png")))
//    {
//      byte[] buffer = new byte[1024];
//      int length;
//      while ((length = from.read(buffer)) > 0) {
//        to.write(buffer, 0, length);
//      }
//    }
//    SpringApplication.run(CommunicationApplication.class, args);
//  }
//
//}