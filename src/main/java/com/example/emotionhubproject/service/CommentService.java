package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.UserEntity;

import java.util.List;
import java.util.Map;

public interface CommentService {
    List<Map<String, Object>> getComments(Long articleId, UserEntity loginUser);
}
