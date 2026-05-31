package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
public class ArticleUpdateForm {
    private Long id;
    private String title;
    private String content;

}
