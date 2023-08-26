package com.Comunicator.controller;


import com.Comunicator.Utils.ConvertData;
import com.Comunicator.model.LastConversationWith;
import com.Comunicator.model.User;
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
    MessageService messageService;

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
    public List<Map<String, Object>> showAllMessagesWithUser(@RequestParam String selectedUserId, Principal principal){
       int principalUser = Math.toIntExact(userRepository.findByName(principal.getName()).getId());
        return messageService.getListMessage(principalUser,Integer.parseInt(selectedUserId));
    }

    @PostMapping("/findLastConversations")
    @ResponseBody
    public List<LastConversationWith> findLastConversations(Principal principal){
        Integer currentUserId = Math.toIntExact(userRepository.findByName(principal.getName()).getId());
       Set<Integer> listOfUsersId =  messageService.getUserIdThatHaveConversationWithLoginUser(currentUserId);

        List<LastConversationWith> lastConversationWithList = new ArrayList<>();

        for(Integer userId: listOfUsersId){
            lastConversationWithList.add(
                    new LastConversationWith(messageService.findLastMessage(currentUserId,userId),
                    userRepository.findById(userId.longValue()),currentUserId,
                    ConvertData.convertLocalDatTime(messageService.findDateOfLastMessage(currentUserId,userId)),
            messageService.findLastSender(currentUserId,userId))
                    );
        }

        return lastConversationWithList;
    }

}
