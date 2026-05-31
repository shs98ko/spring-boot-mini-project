package com.example.emotionhubproject.entity;

//EmotionDiary

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Getter
@Setter
@AllArgsConstructor @NoArgsConstructor @ToString
public class EmotionDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String emotion;

    private int score;

    private String content;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public EmotionDiary(String emotion, String content, int score) {
        this.emotion = emotion;
        this.content = content;
        this.score = score;
    }

    public String getFormattedCreatedAt() {
        return createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}
