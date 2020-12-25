package com.example.communication;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;

import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.jdbc.Sql;

@WithUserDetails(value = "admin")
@Sql(value = {"/messages_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = {"/messages_after.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class MainPageTest extends AbstractSpringTest{

  @Test
  public void messageListTest() throws Exception {
//    ArrayList<User> actualUsers = messageRepository.find;
//    assertEquals();
  }

  @Test
  public void navbarNameTest() throws Exception {
    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(authenticated())
        .andExpect(xpath("//*[@id='navbarUsername']/div").string("\n"
            + "                      admin\n"
            + "  \n"
            + "                    "));
    //*[@id="navbarUsername"]/div
    //*[@id="navbarUsername"]
    //*[@id="dropdownMenu2"]/div
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
    //*[@id="messages"]/div[1]/div[1]/div[1]/h5/a
    //*[@id='message-list']/div
  }
}
