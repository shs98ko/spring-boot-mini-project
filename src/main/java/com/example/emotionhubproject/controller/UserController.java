package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.ChangePasswordForm;
import com.example.emotionhubproject.dto.JoinForm;
import com.example.emotionhubproject.dto.LoginForm;
import com.example.emotionhubproject.dto.UserUpdateForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.UserServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserController {

    private final UserServiceImpl userServiceImpl;

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
            userServiceImpl.join(joinForm);
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
            UserEntity user = userServiceImpl.login(loginForm);
            HttpSession session = request.getSession();
            session.setAttribute("loggedIn", true);
            session.setAttribute("user", user); //DTO 고려
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

    @GetMapping("/users/{id}")
    public String seeUser(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes){
        try {
            UserEntity user = userServiceImpl.getUserByUserId(id);
            List<Article> articles = userServiceImpl.getArticlesByUserId(user.getId());
            model.addAttribute("pageTitle", user.getUsername() + "상세페이지");
            model.addAttribute("user", user);
            model.addAttribute("articles", articles);
            return"users/profile";
        }catch(ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/users/{id}/edit")
    public String getEdit(@PathVariable Long id, Model model){
        UserEntity updatedUser = userServiceImpl.getUserByUserId(id);
        model.addAttribute("pageTitle", "Edit");
        model.addAttribute("user", updatedUser);
        return"users/edit-profile";
    }
    @PostMapping("/users/{id}/edit")
    public String postEdit(@PathVariable Long id, Model model, UserUpdateForm userUpdateForm,
                           RedirectAttributes redirectAttributes, HttpServletRequest request) {
        try {
            if(isUnauthorized(request, id, redirectAttributes)){
                return "redirect:/";
            }
            userServiceImpl.saveUpdateUser(userUpdateForm, id);
            model.addAttribute("pageTitle", "Edit");
            return"redirect:/users/"+ id;
        } catch (ErrorMessageException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/";
        }

    }

    @GetMapping("/users/{id}/changepassword")
    public String getChangePassword(Model model){
        model.addAttribute("pageTitle","비밀번호 변경");
        return "users/change-password";
    }


    @PostMapping("/users/{id}/changepassword")
    public String postChangePassword(@PathVariable Long id,Model model,
                                     ChangePasswordForm changePasswordForm, HttpServletRequest request,
                                     RedirectAttributes redirectAttributes){
        try {
            if(isUnauthorized(request, id, redirectAttributes)){
                return "redirect:/";
            }
            userServiceImpl.changePassword(changePasswordForm,id);
            model.addAttribute("pageTitle", "Edit");
            return"redirect:/users/"+ id;

        } catch (ErrorMessageException e) {
            redirectAttributes.addFlashAttribute("errorMessage",e.getMessage());
            return "redirect:/users/{id}/changepassword";
        }
    }

    private boolean isUnauthorized(HttpServletRequest request, Long id, RedirectAttributes redirectAttributes) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다");
            return true;
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "로그인이 필요합니다");
            return true;
        }
        if (!loginUser.getId().equals(id)) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다");
            return true;
        }
        return false;
    }


}
