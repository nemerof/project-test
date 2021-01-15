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
@Sql(value = {"/user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/user_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ChatTest extends AbstractSpringTest {

  @Test
  public void userListTest() throws Exception {
    this.mockMvc.perform(get("/chat"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//*[@id='chatUserlist']").string("\n"
            + "          \n"
            + "            \n"
            + "              \n"
            + "                \n"
            + "  \n"
            + "                \n"
            + "                \n"
            + "                    usr\n"
            + "                \n"
            + "              \n"
            + "            \n"
            + "          \n"
            + "          \n"
            + "            \n"
            + "              \n"
            + "                \n"
            + "  \n"
            + "                \n"
            + "                \n"
            + "                    user\n"
            + "                \n"
            + "              \n"
            + "            \n"
            + "          \n"
            + "        "));
  }
}
