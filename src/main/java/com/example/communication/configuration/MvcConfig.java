package com.example.communication.configuration;

import com.example.communication.interceptor.CommunicationInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Configuration
public class MvcConfig implements WebMvcConfigurer {
  @Value("${upload.path}")
  private String uploadPath;

  @Value("${spring.datasource.url}")
  private String dbUrl;

  @Value("${spring.datasource.username}")
  private String user;

  @Value("${spring.datasource.password}")
  private String password;

  private final CommunicationInterceptor interceptor;

  public MvcConfig(
      CommunicationInterceptor interceptor) {
    this.interceptor = interceptor;
  }

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/login").setViewName("login");
  }

  @Override
  public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry.addResourceHandler("/img/**")
        .addResourceLocations("file://" + uploadPath + "/");
    registry.addResourceHandler("/static/**")
        .addResourceLocations("classpath:/static/");
  }

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(interceptor);
  }

  @Bean
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(dbUrl, user, password);
  }

  @Bean
  public JdbcTemplate getJdbcTemplate() {
    return new JdbcTemplate(new DriverManagerDataSource(dbUrl, user, password));
  }
}
