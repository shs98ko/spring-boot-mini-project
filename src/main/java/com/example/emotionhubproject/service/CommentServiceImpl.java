package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.CommentResponse;
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

    public List<CommentResponse> getComments(Long articleId, UserEntity loginUser) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        loginUser != null && loginUser.getId().equals(comment.getUserId())
                ))
                .collect(Collectors.toList());
    }


}
