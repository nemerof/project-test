package com.example.communication.model;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Comment extends AbstractMessageEntity{

  @ManyToOne(fetch = FetchType.EAGER)
  @OnDelete(action = OnDeleteAction.CASCADE)
  @JoinTable(
          name = "message_comments",
          joinColumns = { @JoinColumn(name = "comments_id") }
  )
  private Message message;

  public Comment(String text, User user, Message message) {
    super(text, user);
    this.message = message;
  }

  public Comment(Long id, String text, String filename, LocalDateTime postTime, User user, Message message) {
    super(id, text, filename, postTime, user);
    this.message = message;
  }

  public Comment(String text, String filename, LocalDateTime postTime, User user, Message message) {
    super(text, filename, postTime, user);
    this.message = message;
  }

  @Override
  public String toString() {
    return "Comment{" +
            "text=" + super.getText() +
            ", author=" + super.getUser().getUsername() +
            "}\n";
  }
}
