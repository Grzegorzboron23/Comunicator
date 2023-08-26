package com.Comunicator.controller;


import com.Comunicator.Utils.ConvertData;
import com.Comunicator.model.LastConversationWith;
import com.Comunicator.model.Message;
import com.Comunicator.model.User;
import com.Comunicator.respository.MessageRepository;
import com.Comunicator.respository.UserRepository;
import com.Comunicator.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Controller
public class MainPageController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MessageRepository messageRepository;

    @GetMapping("/mainPage")
    public String mainPage(HttpServletRequest request, Model model, Principal principal) {

        model.addAttribute("loggedInUser", principal.getName());
        String searchInput = request.getParameter("searchInput");

       User user = userRepository.findByName(principal.getName());

        model.addAttribute("userId", user.getId());

        if(searchInput != null) {
            List<User> userList = userRepository.findByUsernameContaining(searchInput);
            model.addAttribute("userList", userList);
        }

        return "mainPage";
    }



    @PostMapping("/search-users")
    public String searchUsers(HttpServletRequest request, Model model) {
        String searchInput = request.getParameter("searchInput");
        return "redirect:/mainPage?searchInput=" + searchInput;
    }

    @PostMapping("/processData")
    @ResponseBody
    public List<User> processData(@RequestParam String inputValue ) {
        if(inputValue != null) {
            List<User> userList = userRepository.findByUsernameContaining(inputValue);
            return userList;
        }
        return null;
    }

    @PostMapping("/showAllMessagesWithUser")
    @ResponseBody
    public List<Message> showAllMessagesWithUser(@RequestParam String selectedUserId, Principal principal){
       User principalUser = userRepository.findByName(principal.getName());
       User findUser = userRepository.findById(Long.valueOf(selectedUserId)).orElse(null);

        List<Message> messagesFromTo = messageRepository.findAllByUserFromAndUserToOrderByDateTimeAsc(principalUser, findUser);
        List<Message> messagesToFrom = messageRepository.findAllByUserToAndUserFromOrderByDateTimeAsc(principalUser, findUser);

        List<Message> allMessages = new ArrayList<>();
        allMessages.addAll(messagesFromTo);
        allMessages.addAll(messagesToFrom);
        Collections.sort(allMessages, Comparator.comparing(Message::getDateTime));

        return allMessages;
    }

    @PostMapping("/findLastConversations")
    @ResponseBody
    public List<Message> findLastConversations(Principal principal){
        User loginUser =userRepository.findByName(principal.getName());

        List<User> allUsersWithConversations = messageRepository.findUsersWithMessagesForUser(loginUser);
        List<Message> lastMessages = new ArrayList<>();

        for(User currentUser: allUsersWithConversations){
            lastMessages.add(messageRepository.findLastMessageByUsers(loginUser,currentUser));
        }




        return lastMessages;
    }


}
