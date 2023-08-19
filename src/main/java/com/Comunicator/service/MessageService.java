package com.Comunicator.service;

import com.Comunicator.model.Message;
import com.Comunicator.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    private final JdbcTemplate jdbcTemplate;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserRepository userRepository;

    @Autowired
    public MessageService(JdbcTemplate jdbcTemplate, SimpMessagingTemplate simpMessagingTemplate, UserRepository userRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userRepository = userRepository;
    }

    public void sendMessage(String to, Message message) {
        jdbcTemplate.update("INSERT INTO messages (message_text, message_from, message_to, date_time) " +
                "VALUES (?, ?, ?, current_timestamp)", message.getMessage(), Integer.valueOf(message.getFromLogin()), Integer.valueOf(to));
        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }

    public List<Map<String, Object>> getListMessage(@PathVariable("from") Integer from, @PathVariable("to") Integer to) {
        return jdbcTemplate.queryForList("SELECT * FROM messages where (message_from=? and message_to=?) " +
                "OR (message_to=? and message_from=?) ORDER BY date_time asc", from, to, from, to);
    }



}
