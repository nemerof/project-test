package com.example.communication.data;

import static com.example.communication.data.MessageTestData.ADMIN_MESSAGE;
import static com.example.communication.data.MessageTestData.USER_MESSAGE;
import static com.example.communication.data.UserTestData.ADMIN;
import static com.example.communication.data.UserTestData.USER;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

public class CommentTestData {

  public static final Comment ADMIN_COMMENT = new Comment
      (3L, "Admin comment", "admin_test_message.png",
          LocalDateTime.of(2020, Month.of(12), 12, 15, 45), ADMIN, new Message(ADMIN_MESSAGE));

  public static final Comment USER_COMMENT = new Comment
      (4L, "User comment", "user_test_message.png",
          LocalDateTime.of(2020, Month.of(12), 12, 16, 12), USER, new Message(USER_MESSAGE));

  public static final List<Comment> COMMENTS = Arrays.asList(ADMIN_COMMENT, USER_COMMENT);
  public static final Comment[] COMMENTS_ARRAY = {ADMIN_COMMENT, USER_COMMENT};

  public static Comment getNew() {
    return new Comment (5L, "Admin comment 2", "admin_test_message.png",
            LocalDateTime.of(2020, Month.of(12), 14, 12, 45), ADMIN, new Message(USER_MESSAGE));
  }
}
