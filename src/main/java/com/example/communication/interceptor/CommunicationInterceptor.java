package com.example.communication.interceptor;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class CommunicationInterceptor extends HandlerInterceptorAdapter {

  private HttpSession session;

  private final UserRepository userRepository;

  public CommunicationInterceptor(
          HttpSession session, UserRepository userRepository) {
    this.session = session;
    this.userRepository = userRepository;
  }

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
        try {
          user = userRepository.findById(user.getId()).get();
        } catch (Exception e) {
          session.invalidate();
        }
//        modelAndView.getModelMap().addAttribute("profilePic", user.getProfilePic());
//        modelAndView.getModelMap().addAttribute("username", user.getUsername());
        request.setAttribute("profilePic", user.getProfilePic());
        request.setAttribute("username", user.getUsername());
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getPassword(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
  }
}
