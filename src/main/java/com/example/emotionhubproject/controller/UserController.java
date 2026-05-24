package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.JoinForm;
import com.example.emotionhubproject.dto.LoginForm;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    public String getJoin(HttpServletRequest request ,Model model){

        //회원가입한사람은 하지 못하게 함
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null){
            return "redirect:/";
        }

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
    public String getLogin(HttpServletRequest request, Model model){
        // 이미 로그인한 사람은 재로그인 하지 못하게 함
        HttpSession session = request.getSession(false);
        if(session != null && session.getAttribute("user") != null){
            return "redirect:/";
        }

        model.addAttribute("pageTitle", "Login");
        return "login";
    }
    @PostMapping("/login")
    public String postLogin(HttpServletRequest request, LoginForm loginForm, Model model){
        try{
            UserEntity user = userService.login(loginForm);
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", true);
            session.setAttribute("user", user);
            return "redirect:/";

        }catch(ErrorMessageException e){
            model.addAttribute("pageTitle","Login");
            model.addAttribute("errorMessage",e.getMessage());
            return "login";
        }
    }

    @GetMapping("/users/logout")
    public String logout(HttpServletRequest request){
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/";
    }


}
