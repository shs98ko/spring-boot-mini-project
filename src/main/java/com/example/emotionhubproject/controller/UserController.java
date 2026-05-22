package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.JoinForm;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/join")
    public String getJoin( Model model){
        model.addAttribute("pageTitle", "Join");
        return "join";

    }

    @PostMapping("/join")
    public String postJoin(JoinForm joinForm, Model model) {
        try{
            userService.join(joinForm);
            return "redirect:/login";
        } catch (ErrorMessageException e) {
            model.addAttribute("pageTitle","Join");
            model.addAttribute("errorMessage",e.getMessage());
            return "join";
        }

    }

    @GetMapping("/login")
    public String getLogin(Model model){
        model.addAttribute("pageTitle", "Login");
        return "login";
    }

}
