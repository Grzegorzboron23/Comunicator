package com.Comunicator.controller;


import com.Comunicator.model.User;
import com.Comunicator.respository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;
import java.util.List;

@Controller
public class MainPageController {

    @Autowired
    UserRepository userRepository;

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
    public String processData(@RequestBody String inputValue) {
        System.out.println("jestem w kontrolerze springa " + inputValue);


        String processedValue = "Przetworzona wartość: " + inputValue;
        return processedValue;
    }

}
