package com.example.communication.service;

import com.example.communication.model.User;
import com.example.communication.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

  private final UserRepository repository;

  public UserService(UserRepository repository) {
    this.repository = repository;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    UserDetails details = repository.findByUsername(s);
    if (details == null)
      details = new User();
    return details;
  }

  public void subscribe(User currentUser, User user) {
    user.getSubscribers().add(currentUser);

    repository.save(user);
  }

  public void unsubscribe(User currentUser, User user) {
    user.getSubscribers().remove(currentUser);

    repository.save(user);
  }
}
