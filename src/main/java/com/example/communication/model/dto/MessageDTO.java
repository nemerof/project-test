package com.example.communication.model.dto;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;

@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private User user;
    private String filename;
    private LocalDateTime postTime;
    private Long likes;
    private Boolean meLiked;
    private Set<Comment> comments;

    public MessageDTO(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.user = message.getUser();
        this.filename = message.getFilename();
        this.postTime = message.getPostTime();
        this.likes = likes;
        this.meLiked = meLiked;
        this.comments = message.getComments();
    }

    public MessageDTO(Long id, String text, User user, String filename,
        LocalDateTime postTime, Long likes,
        Set<Comment> comments) {
        this.id = id;
        this.text = text;
        this.user = user;
        this.filename = filename;
        this.postTime = postTime;
        this.likes = likes;
        this.comments = comments;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
            "id=" + id +
            ", text='" + text + '\'' +
            ", user=" + user +
            ", filename='" + filename + '\'' +
            ", postTime=" + postTime +
            ", likes=" + likes +
            ", comments=" + comments +
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MessageDTO)) {
            return false;
        }
        MessageDTO that = (MessageDTO) o;
        return getId().equals(that.getId()) && Objects.equals(getText(), that.getText())
            && getUser().equals(that.getUser()) && Objects
            .equals(getFilename(), that.getFilename()) && getPostTime().equals(that.getPostTime())
            && Objects.equals(getLikes(), that.getLikes()) && Objects
            .equals(getComments(), that.getComments());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
