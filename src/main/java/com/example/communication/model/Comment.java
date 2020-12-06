package com.example.communication.model;

import javax.persistence.*;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Comment extends AbstractMessageEntity{

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinTable(
          name = "message_comments",
          joinColumns = { @JoinColumn(name = "comments_id") }
  )
  private Message message;

  public Comment(String text, User user, Message message) {
    super(text, user);
    this.message = message;
  }

  @Override
  public String toString() {
    return "Comment{" +
            "text=" + super.getText() +
            " " + super.getUser().getUsername() +
            "}\n";
  }
}
