package com.example.communication.model.dto;

import com.example.communication.model.Comment;
import com.example.communication.model.Message;
import com.example.communication.model.User;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;

@Getter
public class MessageDTO {
    private final Long id;
    private final String text;
    private final User user;
    private final String filename;
    private final LocalDateTime postTime;
    private final Long likes;
    private Boolean meLiked;
    private final Set<Comment> comments;

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
            '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDTO that = (MessageDTO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
