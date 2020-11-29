package com.example.communication.model.dto;

import com.example.communication.model.Message;
import com.example.communication.model.User;
import lombok.Getter;

@Getter
public class MessageDTO {
    private Long id;
    private String text;
    private User user;
    private String filename;
    private Long likes;
    private Boolean meLiked;

    public MessageDTO(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.user = message.getUser();
        this.filename = message.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    @Override
    public String toString() {
        return "MessageDTO{" +
                "id=" + id +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
