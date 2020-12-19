package com.example.communication;

import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.junit.Test;
import org.springframework.security.test.context.support.WithUserDetails;

@WithUserDetails(value = "dru")
public class MainPageTest extends AbstractSpringTest{

  @Test
  public void mainPageTest() throws Exception {
    this.mockMvc.perform(get("/main"))
        .andDo(print())
        .andExpect(authenticated());
  }
}
