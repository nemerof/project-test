package com.example.communication.model;

import java.util.Collection;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Data
@Entity
@Table(name = "usr")
public class User implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Column(unique = true)
  @NotEmpty(message = "Username cannot be empty")
  @Size(min = 3, max = 16, message = "Username should be in range from 3 to 16 symbols")
  private String username;

  @NotEmpty(message = "Password cannot be empty")
  @Size(min = 6, max = 16, message = "Password should be in range from 6 to 16 symbols")
  private String password;

  @Column(unique = true)
  @NotEmpty(message = "Email cannot be empty")
  private String email;

  private boolean active;

  @ElementCollection(targetClass = Role.class, fetch = FetchType.EAGER)
  @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
  @Enumerated(EnumType.STRING)
  private Set<Role> roles;

  public User(String username, String password, String email, boolean active) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.active = active;
  }

  public User(
      @Size(min = 6, max = 16) String username,
      @Size(min = 6, max = 16) String password,
      @Email String email, boolean active, Set<Role> roles) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.active = active;
    this.roles = roles;
  }

  public User() {
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return getRoles();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return isActive();
  }

  public boolean isAdmin() {
    return roles.contains(Role.ADMIN);
  }

  //Bad
  public boolean isAuthorized() {
    return !roles.isEmpty();
  }



  @Override
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  @Override
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public boolean isActive() {
    return active;
  }

  public void setActive(boolean active) {
    this.active = active;
  }

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }
}
