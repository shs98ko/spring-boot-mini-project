package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class ArticleUpdateForm {
    private Long id;
    private String title;
    private String content;

    public void logInfo(){
        log.info("title: {}, content: {}", title, content);
    }
}
