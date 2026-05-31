package com.example.emotionhubproject.dto;

import lombok.*;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
public class CommentResponse {
    private Long id;
    private String text;
    private boolean canDelete;
}
