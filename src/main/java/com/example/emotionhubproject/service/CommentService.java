package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.CommentResponse;
import com.example.emotionhubproject.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<CommentResponse> getComments(Long articleId, UserEntity loginUser);
}
