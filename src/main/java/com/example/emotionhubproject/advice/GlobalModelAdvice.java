package com.example.emotionhubproject.advice;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

//로그인 정보를 전역변수로 하여 mustache에 화면을 렌더링할 때 정보를 넘겨줌
@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute
    public void  addGlobalAttributes(HttpServletRequest request, Model model){
        HttpSession session = request.getSession(false);

        if(session == null) {
            model.addAttribute("loggedIn",false);
            model.addAttribute("loggedInUser",null);
            return;
        }

        Boolean loggedIn = (Boolean) session.getAttribute("loggedIn");
        model.addAttribute("loggedIn", loggedIn != null && loggedIn);
        model.addAttribute("loggedInUser", session.getAttribute("user"));

    }
}
