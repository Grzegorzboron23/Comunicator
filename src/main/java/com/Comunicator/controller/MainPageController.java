package com.Comunicator.controller;


import com.Comunicator.model.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
public class MainPageController {

    @GetMapping("/mainPage")
    public String mainPage(Model model,Principal principal) {
        model.addAttribute("loggedInUser", principal.getName());
        return "mainPage";
    }

}
