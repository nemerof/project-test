package com.example.communication.integration;


import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.communication.AbstractSpringTest;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

public class LoginTest extends AbstractSpringTest {

  @Test
  public void accessDeniedTest() throws Exception {
    this.mockMvc.perform(get("/"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("http://localhost/login"));
  }

  @Test
  @Sql(value = {"/user_before.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
  public void correctLoginTest() throws Exception {
    this.mockMvc.perform(formLogin().user("admin").password("admin123"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
  }

  @Test
  public void badCredentials() throws Exception {
    this.mockMvc.perform(post("/login").param("username", "password"))
        .andDo(print())
        .andExpect(status().isForbidden());
  }
}
