package com.example.emotionhubproject.api;


import com.example.emotionhubproject.dto.CommentForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.Comment;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.repository.CommentRepository;
import com.example.emotionhubproject.service.CommentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api")
@RestController
public class CommentApiController {

    private final CommentServiceImpl commentServiceImpl;

    @PostMapping("/articles/{id}/comment")
    public ResponseEntity<?> createComment(HttpServletRequest request, @PathVariable Long id, @RequestBody CommentForm commentForm){
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Comment commentSaved = commentServiceImpl.createComment(id, commentForm, loginUser);

        //JSON형식으로 변환
        Map<String, Long> response = new HashMap<>();
        response.put("newCommentId", commentSaved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable Long commentId){
        HttpSession session = request.getSession(false);
        if (session == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentServiceImpl.deleteComment(commentId, loginUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
