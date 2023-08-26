package com.Comunicator.service;

import com.Comunicator.model.Message;
import com.Comunicator.model.User;
import com.Comunicator.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.*;

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
        return jdbcTemplate.queryForList("SELECT * FROM messages WHERE (message_from=? and message_to=?) " +
                "OR (message_to=? and message_from=?) ORDER BY date_time asc", from, to, from, to);
    }

    public Set<Integer> getUserIdThatHaveConversationWithLoginUser(Integer userId){
        List<Map<String, Object>>  listOfUsersIds =  jdbcTemplate.queryForList(
                "SELECT DISTINCT message_from, message_to, date_time, message_text FROM messages WHERE message_from = ? OR message_to = ? ORDER BY date_time ASC",
                userId, userId);

        Set<Integer> usersId = new HashSet<>();
        for (Map<String, Object> userMap : listOfUsersIds) {
                usersId.add((Integer) userMap.get("message_from"));
                usersId.add((Integer) userMap.get("message_to"));
        }
        return usersId;
    }


    public String findLastMessage(Integer currentUserId, Integer userId) {
        try {
            String message = jdbcTemplate.queryForObject(
                    "SELECT message_text FROM messages WHERE (message_from = ? AND message_to = ?) OR (message_to = ? AND message_from = ?) ORDER BY date_time ASC LIMIT 1",
                    String.class, currentUserId, userId, currentUserId, userId);
            return message;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }


    public LocalDateTime findDateOfLastMessage(Integer currentUserId, Integer userId) {
        try {
            LocalDateTime  localDateTime = jdbcTemplate.queryForObject(
                    "SELECT date_time FROM messages WHERE (message_from = ? AND message_to = ?) OR (message_to = ? AND message_from = ?) ORDER BY date_time ASC LIMIT 1",
                    LocalDateTime.class, currentUserId, userId, currentUserId, userId);
            return localDateTime;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public User findLastSender(Integer currentUserId, Integer userId) {
        try {
            Integer senderId = jdbcTemplate.queryForObject(
                    "SELECT CASE WHEN message_from = ? THEN message_to ELSE message_from END AS sender_id FROM messages WHERE (message_from = ? AND message_to = ?) OR (message_to = ? AND message_from = ?) ORDER BY date_time ASC LIMIT 1",
                    Integer.class, currentUserId, currentUserId, userId, currentUserId, userId
            );
            return userRepository.getById(Long.valueOf(senderId));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }
}
