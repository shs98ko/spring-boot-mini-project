package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.CommentForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.Comment;
import com.example.emotionhubproject.dto.CommentResponse;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

// CommentService
@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public List<CommentResponse> getComments(Long articleId, UserEntity loginUser) {
        return commentRepository.findByArticleId(articleId).stream()
                .map(comment -> new CommentResponse(
                        comment.getId(),
                        comment.getText(),
                        loginUser != null && loginUser.getId().equals(comment.getUserId())
                ))
                .collect(Collectors.toList());
    }

    public Comment createComment(Long articleId, CommentForm commentForm, UserEntity loginUser) {
        Article article = articleRepository.findById(articleId).orElseThrow(() -> new ErrorMessageException("게시글을 찾을 수 없습니다."));
        Comment comment = new Comment(loginUser.getId(), article.getId(), commentForm.getText());
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, UserEntity loginUser) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ErrorMessageException("댓글을 찾을 수 없습니다."));

        if (!comment.getUserId().equals(loginUser.getId())) {
            throw new ErrorMessageException("권한이 없습니다.");
        }
        commentRepository.delete(comment);
    }



}
