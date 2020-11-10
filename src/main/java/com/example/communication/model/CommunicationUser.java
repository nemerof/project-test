package com.example.communication.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.Size;
import lombok.Data;


@Data
@Entity
public class CommunicationUser{

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @Size(min = 3, max = 15)
  private String name;

  @Size(min = 4, max = 25)
  private String password;

  @Email
  private String email;

  public CommunicationUser(
      @Size(min = 3, max = 15) String name,
      @Size(min = 4, max = 25) String password,
      @Email String email) {
    this.name = name;
    this.password = password;
    this.email = email;
  }

  public CommunicationUser() {
  }
}
