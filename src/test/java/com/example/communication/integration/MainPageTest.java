package com.example.communication.integration;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import com.example.communication.AbstractSpringTest;
import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@WithUserDetails(value = "admin")
@Sql(value = {"/messages_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/comments_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/comments_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(value = {"/message_likes_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/message_likes_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainPageTest extends AbstractSpringTest {

  @Test
  public void navbarNameTest() throws Exception {
    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//*[@id='navbarUsername']/div").string("\n"
            + "                      admin\n"
            + "  \n"
            + "                    "));
  }

  @Test
  public void messageTextTest() throws Exception {
    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//*[@id='messageText']").string("\n"
            + "            \n"
            + "              Admin message\n"
            + "            \n"
            + "          "));
  }
}
