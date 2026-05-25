package com.example.emotionhubproject.api;


import com.example.emotionhubproject.dto.CommentForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.Comment;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.repository.CommentRepository;
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

    private final ArticleRepository articleRepository;

    private final CommentRepository commentRepository;
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

        Article article = articleRepository.findById(id)
                .orElse(null);

        if (article == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("errorMessage", "Article not found"));
        }

        //Comment 생성 및 저장
        Comment comment = new Comment(loginUser.getId(), article.getId(), commentForm.getText());
        Comment commentSaved = commentRepository.save(comment);

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

        Comment comment = commentRepository.findById(commentId).orElse(null);

        if (comment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        if(!comment.getUserId().equals(loginUser.getId())){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        commentRepository.delete(comment);

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
