package com.example.communication.module;

import static com.example.communication.data.MessageTestData.ADMIN_MESSAGE;
import static com.example.communication.data.MessageTestData.MESSAGES;
import static com.example.communication.data.MessageTestData.USER_MESSAGE;
import static com.example.communication.data.UserTestData.ADMIN;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.communication.AbstractSpringTest;
import com.example.communication.controller.ControllerUtils;
import com.example.communication.data.MessageTestData;
import com.example.communication.model.dto.MessageDTO;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@WithUserDetails(value = "admin")
@Sql(value = {"/user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/comments_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/comments_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/message_likes_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/message_likes_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)

public class MainPageTest extends AbstractSpringTest {

  @Test
  public void messageListTest() {
    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("", ADMIN);
    assertEquals(actualMessages, MESSAGES);
  }

  @Test
  public void messageListFilterTest() {
    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("User", ADMIN);
    assertEquals(actualMessages, Arrays.asList(USER_MESSAGE));
  }

  @Test
  public void messageDeleteTest() {
    messageRepository.deleteById(ADMIN_MESSAGE.getId());
    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("", ADMIN);
    assertEquals(actualMessages, Arrays.asList(USER_MESSAGE));
  }

  @Test
  public void messageAddTest() throws IOException {
    ControllerUtils.saveMessage(null, MessageTestData.getNew());
    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("", ADMIN);
    assertEquals(actualMessages.size(), 3);
  }

//  @Test
//  public void messageLikeTest() throws IOException {
//    ControllerUtils.saveMessage(null, MessageTestData.getNew());
//    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("", ADMIN);
//    assertEquals(actualMessages.size(), 3);
//  }

//  @Test
//  public void addCommentTest() throws IOException {
//    ControllerUtils.saveMessage(null, MessageTestData.getNew());
//    List<MessageDTO> actualMessages= messageService.getAllMessagesTest("", ADMIN);
//    assertEquals(actualMessages.size(), 3);
//  }
}
