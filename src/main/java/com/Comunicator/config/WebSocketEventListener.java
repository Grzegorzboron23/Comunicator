package com.Comunicator.config;

import com.Comunicator.model.Message;
import com.Comunicator.model.MessageType;
import com.Comunicator.respository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@Slf4j
public class WebSocketEventListener {


    private final UserRepository userRepository;
    private final SimpMessageSendingOperations messageSendingOperations;

    @Autowired
    public WebSocketEventListener(UserRepository userRepository,SimpMessageSendingOperations messageSendingOperations) {
    this.userRepository = userRepository;
    this.messageSendingOperations = messageSendingOperations;
    }

    @EventListener
    public void handleWebSocketListener(SessionDisconnectEvent event){


        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String username = (String) headerAccessor.getSessionAttributes().get("username");

        if(username !=null){
            log.info("User disconnected: {}", username);
            var chatMessage = Message.builder()
                    .messageType(MessageType.LEAVE)
                    .user(userRepository.findByName(username))
                    .build();

            messageSendingOperations.convertAndSend("/topic/public",chatMessage);
        }
    }

}
