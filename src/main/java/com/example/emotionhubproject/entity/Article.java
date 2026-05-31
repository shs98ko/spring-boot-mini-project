package com.example.emotionhubproject.entity;

import com.example.emotionhubproject.dto.ArticleUpdateForm;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@NoArgsConstructor
@Slf4j
@Getter
@ToString
@Table(name = "article")
public class Article {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(nullable = false)
    private String title;

    @Lob
    private String content;


    private Long userId;
    private String username;


    @Column(name = "created_at", updatable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    private LocalDateTime createdAt;


    public  Article(String title, String content, Long userId, String username){
        this.title = title;
        this.content = content;
        this.userId = userId;
        this.username =username;
    }

    public void logInfo(){
        log.info("id: {}, title: [], content: {}",id , title, content);
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public void patch(ArticleUpdateForm dto){
        if(dto.getTitle() != null){
            this.title = dto.getTitle();
        }
        if(dto.getContent() != null){
            this.content = dto.getContent();
        }
    }

    //본인확인여부
    public boolean isOwner(Long userId){
        return this.userId.equals(userId);
    }
}
