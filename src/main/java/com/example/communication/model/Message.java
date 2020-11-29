package com.example.communication.model;

import javax.persistence.*;

import lombok.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

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

  @ManyToMany
  @JoinTable(
          name = "message_likes",
          joinColumns = { @JoinColumn(name = "message_id") },
          inverseJoinColumns = { @JoinColumn(name = "user_id") }
  )
  private Set<User> likes = new HashSet<>();

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
