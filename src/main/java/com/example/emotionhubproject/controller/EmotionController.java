package com.example.emotionhubproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmotionController {
    @GetMapping("/")
    public String getHome(Model model){
        model.addAttribute("pageTitle", "HOME SWEET HOME");
        //로그아웃 시 세션 제거 확인
        //HttpSession session = request.getSession(false);
        //System.out.println(session);
        return "home";
    }
}
