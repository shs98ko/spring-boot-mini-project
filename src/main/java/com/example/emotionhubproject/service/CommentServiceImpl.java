package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// CommentService
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    public List<Map<String, Object>> getComments(Long articleId, UserEntity loginUser) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(comment -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", comment.getId());
                    map.put("text", comment.getText());
                    map.put("canDelete", loginUser != null && loginUser.getId().equals(comment.getUserId()));
                    return map;
                })
                .collect(Collectors.toList());
    }
}
