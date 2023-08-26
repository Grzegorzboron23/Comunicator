package com.Comunicator.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Optional;

@Getter
@Setter
@AllArgsConstructor
public class LastConversationWith {
    private String messageText;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Optional<User> user;

    private Integer loginUserId;
    private String dateTime;

    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private User senderLastMessage;


}
