package com.example.emotionhubproject.api;


import com.example.emotionhubproject.dto.CommentForm;
import com.example.emotionhubproject.entity.Comment;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.service.CommentService;
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
public class  CommentApiController {

    private final CommentService commentService;

    @PostMapping("/articles/{id}/comment")
    public ResponseEntity<?> createComment(HttpServletRequest request, @PathVariable Long id, @RequestBody CommentForm commentForm){
        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Comment commentSaved = commentService.createComment(id, commentForm, loginUser);

        //JSON형식으로 변환
        Map<String, Long> response = new HashMap<>();
        response.put("newCommentId", commentSaved.getId());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

        //return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("newCommentId", saved.getId()));
    }
    @DeleteMapping("/comments/{commentId}/delete")
    public ResponseEntity<?> deleteComment(HttpServletRequest request, @PathVariable Long commentId){
        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        commentService.deleteComment(commentId, loginUser);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private UserEntity getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (UserEntity) session.getAttribute("user");
    }
}
