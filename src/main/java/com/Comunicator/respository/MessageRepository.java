package com.Comunicator.respository;

import com.Comunicator.model.Message;
import com.Comunicator.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "((m.userFrom = :loginUser AND m.userTo = :currentUser) OR " +
            "(m.userFrom = :currentUser AND m.userTo = :loginUser)) " +
            "ORDER BY m.dateTime DESC LIMIT 1")
    Message findLastMessageByUsers(User loginUser, User currentUser);


    @Query("SELECT DISTINCT u " +
            "FROM User u, Message m " +
            "WHERE (m.userFrom = :userFrom OR m.userTo = :userFrom) " +
            "AND (u = m.userFrom OR u = m.userTo)")
    List<User> findUsersWithMessagesForUser(@Param("userFrom") User userFrom);
    List<Message> findAllByUserFromAndUserToOrderByDateTimeAsc(User from, User to);
    List<Message> findAllByUserToAndUserFromOrderByDateTimeAsc(User to, User from);

}
