package com.example.communication.model;

import com.example.communication.model.dto.MessageDTO;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Message extends AbstractMessageEntity implements Serializable {

  @OneToMany
  @JoinTable(
          name = "message_comments",
          joinColumns = { @JoinColumn(name = "message_id") }
  )
  private Set<Comment> comments = new HashSet<>();

  public Message(String text, User user) {
    super(text, user);
  }

  public Message(MessageDTO messageDTO) {
    super(messageDTO.getId(), messageDTO.getText(), messageDTO.getText(), messageDTO.getPostTime(),
        messageDTO.getUser());
    comments = messageDTO.getComments();
  }

  public Message(Long id, String text, String filename, LocalDateTime postTime,
      User user, Set<Comment> comments) {
    super(id, text, filename, postTime, user);
    this.comments = comments;
  }

  public Message(String text, String filename, LocalDateTime postTime,
      User user, Set<Comment> comments) {
    super(text, filename, postTime, user);
    this.comments = comments;
  }

  @Override
  public String toString() {
    return String.valueOf(super.getId());
  }

}
