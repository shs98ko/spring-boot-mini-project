package com.example.emotionhubproject.dto;


import com.example.emotionhubproject.entity.Article;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Slf4j
public class ArticleForm {
    private String title;
    private String content;


    public void logInfo(){
        log.info("Article Form => title: {}, content: {}",title,content);
    }
}
