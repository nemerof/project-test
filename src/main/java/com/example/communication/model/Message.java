package com.example.communication.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Message extends AbstractMessageEntity {

  @OneToMany
  @JoinTable(
          name = "message_comments",
          joinColumns = { @JoinColumn(name = "message_id") }
  )
  private Set<Comment> comments = new HashSet<>();


//  @Id
//  @GeneratedValue(strategy = GenerationType.AUTO)
//  private Long id;

//  private String text;
//
//  private String filename;
//
//  private LocalDateTime postTime;

//  @ManyToOne(fetch = FetchType.EAGER)
//  @JoinColumn(name = "user_id")
//  private User user;
//
//  @ManyToMany
//  @JoinTable(
//          name = "message_likes",
//          joinColumns = { @JoinColumn(name = "message_id") },
//          inverseJoinColumns = { @JoinColumn(name = "user_id") }
//  )
//  private Set<User> likes = new HashSet<>();

//  public Message(String text, User user) {
//    this.text = text;
//    this.user = user;
//  }

  public Message(String text, User user) {
    super(text, user);
  }

  @Override
  public String toString() {
    return String.valueOf(super.getId());
  }

  //  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Message message = (Message) o;
//    return Objects.equals(id, message.id);
//  }
//
//  @Override
//  public int hashCode() {
//    return Objects.hash(id);
//  }
}
