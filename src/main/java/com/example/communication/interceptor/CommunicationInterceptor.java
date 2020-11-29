package com.example.communication.interceptor;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CommunicationInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private UserRepository userRepository;

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
    if (modelAndView != null && !modelAndView.isEmpty()) {
      Authentication o1 = SecurityContextHolder.getContext().getAuthentication();
      if (o1 == null)
        return;
      Object o2 = o1.getPrincipal();
      User user = null;
      if (!o2.toString().equals("anonymousUser")) {
        user = (User) o2;
      }
      if (user != null) {
        user = userRepository.findById(user.getId()).get();
        modelAndView.getModelMap().addAttribute("username", user.getUsername());
      }
    }
  }
}
