package com.Comunicator.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="messages")
public class Message {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "message_text")
    private String message;

    @Column(name = "message_from", nullable = false)
    private int fromLogin;

    @Column(name = "message_to", nullable = false)
    private int toLogin;

    @Column(name = "date_time",nullable = false)
    private LocalDateTime dateTime;



    public Message(String message, int fromLogin, int toLogin) {
        this.message = message;
        this.fromLogin = fromLogin;
        this.toLogin = toLogin;
        this.dateTime = LocalDateTime.now();
    }

}
