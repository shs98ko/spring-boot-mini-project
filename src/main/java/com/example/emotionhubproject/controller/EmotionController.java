package com.example.emotionhubproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmotionController {
    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("pageTitle", "HOME SWEET HOME");
        return "home";
    }
}
