package com.example.communication.configuration;

import com.example.communication.service.UserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserService service;

    private final PasswordEncoder encoder;

    public WebSecurityConfig(UserService service, PasswordEncoder encoder) {
        this.service = service;
        this.encoder = encoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .sessionManagement()
                .maximumSessions(1)
              .and()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
              .and()
                .authorizeRequests()
                .antMatchers("/registration", "/static/**", "/activate/*").permitAll()
                .antMatchers("/registration", "/login").access("!isAuthenticated()")
                .anyRequest().authenticated()
              .and()
                .formLogin()
                .loginPage("/login")
              .and()
                .rememberMe()
              .and()
                .logout()
                .deleteCookies("JSESSIONID")
                .permitAll();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(service)
                .passwordEncoder(encoder);
    }
}
