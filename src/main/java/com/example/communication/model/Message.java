package com.example.communication.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
public class Message {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  private String text;

  private String filename;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  private User user;

  public Message(String text, User user) {
    this.text = text;
    this.user = user;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Message message = (Message) o;
    return Objects.equals(id, message.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
