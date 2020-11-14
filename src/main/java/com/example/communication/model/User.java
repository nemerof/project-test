package com.example.communication.model;

import java.util.Set;
import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
@Table(name = "usr")
public class User{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Size(min = 3, max = 15)
  private String username;

  @Size(min = 4, max = 25)
  private String password;

  @Email
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

  public User() {
  }
}
