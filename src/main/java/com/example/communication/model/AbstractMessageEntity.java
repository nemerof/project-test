package com.example.communication.model;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@MappedSuperclass
@Access(AccessType.FIELD)
@Getter
@Setter
@ToString
public abstract class AbstractMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String text;

    private String filename;

    private LocalDateTime postTime;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "message_likes",
            joinColumns = {@JoinColumn(name = "message_id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id")}
    )
    private Set<User> likes = new HashSet<>();

    public AbstractMessageEntity(String text, User user) {
        this.text = text;
        this.user = user;
    }

    public AbstractMessageEntity() {
    }

    //test constructors
    public AbstractMessageEntity(Long id, String text, String filename,
                                 LocalDateTime postTime, User user) {
        this.id = id;
        this.text = text;
        this.filename = filename;
        this.postTime = postTime;
        this.user = user;
    }

    public AbstractMessageEntity(String text, String filename,
                                 LocalDateTime postTime, User user) {
        this.text = text;
        this.filename = filename;
        this.postTime = postTime;
        this.user = user;
    }
    //  @Override
//  public boolean equals(Object o) {
//    if (this == o) return true;
//    if (o == null || getClass() != o.getClass()) return false;
//    Message message = (Message) o;
//    return Objects.equals(id, message.id);
//  }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractMessageEntity message = (Message) o;
        return Objects.equals(id, message.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
