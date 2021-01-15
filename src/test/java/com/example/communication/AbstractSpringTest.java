package com.example.communication;

import com.example.communication.repository.MessageRepository;
import com.example.communication.repository.UserRepository;
import com.example.communication.service.MessageService;
import com.example.communication.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource("/application-test.properties")
//@Sql(value = {"/user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
//@Sql(value = {"/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class AbstractSpringTest {

  @Autowired
  protected UserRepository userRepository;

  @Autowired
  protected UserService userService;

  @Autowired
  protected MessageRepository messageRepository;

  @Autowired
  protected MessageService messageService;

  @Autowired
  protected MockMvc mockMvc;
}
