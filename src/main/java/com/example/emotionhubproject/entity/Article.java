package com.example.emotionhubproject.entity;

import com.example.emotionhubproject.dto.ArticleUpdateDto;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Entity
@NoArgsConstructor
@Slf4j
@Getter
@ToString
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    private String title;
    private String content;
    private Long userId;


    public  Article(String title, String content, Long userId){
        this.title = title;
        this.content = content;
        this.userId = userId;
    }

    public void logInfo(){
        log.info("id: {}, title: [], content: {}",id , title, content);
    }

    public void patch(ArticleUpdateDto dto){
        if(dto.getTitle() != null){
            this.title = dto.getTitle();
        }
        if(dto.getContent() != null){
            this.content = dto.getContent();
        }
    }
}
