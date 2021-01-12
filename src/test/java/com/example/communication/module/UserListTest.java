package com.example.communication.module;

import static com.example.communication.data.UserTestData.USERS;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.communication.AbstractSpringTest;
import com.example.communication.model.User;
import java.util.List;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

@Sql(value = {"/user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class UserListTest extends AbstractSpringTest {

  @Test
  public void messageListTest() {
    List<User> actualUsers= userRepository.findAllTest();
    assertEquals(actualUsers, USERS);
  }
}
