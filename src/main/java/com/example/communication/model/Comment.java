package com.example.communication.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString
@Entity
public class Comment extends AbstractMessageEntity{

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "message_id")
  private Message message;

  public Comment(String text, User user) {
    super(text, user);
  }

}
