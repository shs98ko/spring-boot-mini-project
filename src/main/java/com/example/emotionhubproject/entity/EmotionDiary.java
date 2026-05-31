package com.example.emotionhubproject.entity;

//EmotionDiary

import jakarta.persistence.*;
import lombok.*;

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

    public EmotionDiary(String emotion, String content, int score) {
    }
}
