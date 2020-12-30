package com.example.communication.data;

import static com.example.communication.data.CommentTestData.ADMIN_COMMENT;
import static com.example.communication.data.CommentTestData.USER_COMMENT;
import static com.example.communication.data.UserTestData.ADMIN;
import static com.example.communication.data.UserTestData.USER;

import com.example.communication.model.Comment;
import com.example.communication.model.dto.MessageDTO;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MessageTestData {
  private final static Set<Comment> ADM_MESS_COMMENTS = new HashSet<>();

  public static final MessageDTO ADMIN_MESSAGE = new MessageDTO
      (1L, "Admin message", ADMIN, "admin_test_message.png",
          LocalDateTime.of(2020, Month.of(12), 12, 14, 1), 1L, ADM_MESS_COMMENTS);

  private final static Set<Comment> USR_MESS_COMMENTS = new HashSet<>();

  public static final MessageDTO USER_MESSAGE = new MessageDTO
      (2L, "User message", USER, "user_test_message.png",
          LocalDateTime.of(2020, Month.of(12), 12, 14, 3), 2L, USR_MESS_COMMENTS);

  static {
    ADM_MESS_COMMENTS.add(ADMIN_COMMENT);
    USR_MESS_COMMENTS.add(USER_COMMENT);
  }

  public static final List<MessageDTO> MESSAGES = Arrays.asList(ADMIN_MESSAGE, USER_MESSAGE);
  public static final MessageDTO[] MESSAGE_ARRAY = {ADMIN_MESSAGE, USER_MESSAGE};

  public static MessageDTO getNew() {
    return new MessageDTO(3L, "Admin message 2", ADMIN, "admin_test_message.png",
            LocalDateTime.of(2020, Month.of(12), 12, 14, 5), 2L, USR_MESS_COMMENTS);
  }
}
