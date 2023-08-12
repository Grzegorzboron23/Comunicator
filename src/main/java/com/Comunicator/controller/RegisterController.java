package com.Comunicator.controller;


import com.Comunicator.model.User;
import com.Comunicator.respository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class RegisterController {

    private final UserRepository userRepository;

    @Autowired
    public RegisterController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/register")
    public String registerUser(){
        return "register";
    }


    @PostMapping("/register")
    public String registerUserSave(@RequestParam String name,
                                   @RequestParam String password,
                                   @RequestParam String email,
                                   @RequestParam Integer phone, Model model) {

        List<User> listOfAllUsers = userRepository.findAll();
        User newUser = new User(name,email,phone,password);

        for(User user: listOfAllUsers){
            if(user.getEmail().equals(newUser.getEmail())){

                System.out.println("Email Already Exists");
                model.addAttribute("status","Email Already Exists");
                return "register";
            }else if(user.getName().equals(newUser.getName())){

                System.out.println("Username Already Exists");
                model.addAttribute("status","Username Already Exists");
                return "register";
            }
        }
        userRepository.save(newUser);
        return "redirect:/mainPage";
    }
}
