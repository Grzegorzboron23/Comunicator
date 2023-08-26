package com.Comunicator.service;

import com.Comunicator.model.Message;
import com.Comunicator.model.User;
import com.Comunicator.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
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
        User userTo = userRepository.getById(Long.valueOf(to));
        User userFrom = userRepository.getById((long) message.getFromLogin());



        jdbcTemplate.update("INSERT INTO messages (message_text, message_from, message_to, user_from, user_to, date_time) " +
                        "VALUES (?, ?, ?, ?, ?, current_timestamp)", message.getMessage(),
                userFrom.getId(), Long.valueOf(to), userFrom.getId(), userTo.getId());

        simpMessagingTemplate.convertAndSend("/topic/messages/" + to, message);
    }


    public List<Map<String, Object>> getListMessage(@PathVariable("from") Integer from, @PathVariable("to") Integer to) {
        return jdbcTemplate.queryForList("SELECT * FROM messages WHERE (message_from=? and message_to=?) " +
                "OR (message_to=? and message_from=?) ORDER BY date_time asc", from, to, from, to);
    }




}
