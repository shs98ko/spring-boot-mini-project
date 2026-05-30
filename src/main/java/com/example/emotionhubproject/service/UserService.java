package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.ChangePasswordForm;
import com.example.emotionhubproject.dto.JoinForm;
import com.example.emotionhubproject.dto.LoginForm;
import com.example.emotionhubproject.dto.UserUpdateForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;

import java.util.List;

public interface UserService {
    void join(JoinForm joinForm);
    UserEntity login(LoginForm loginForm);
    UserEntity getUserByUserId(Long id);
    List<Article> getArticlesByUserId(Long id);

    void saveUpdateUser(UserUpdateForm userUpdateForm, Long id);
    void changePassword(ChangePasswordForm changePasswordForm, Long id);
}
