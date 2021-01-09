package com.example.communication.model.chat;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@NoArgsConstructor
@Entity
public class OutputMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String fromU;
    private String toU;
    private String text;
    private String time;

    public OutputMessage(String from, String to, String text, String time) {
        this.fromU = from;
        this.toU = to;
        this.text = text;
        this.time = time;
    }
}
