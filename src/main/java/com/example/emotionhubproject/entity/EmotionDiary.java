package com.example.emotionhubproject.entity;

//EmotionDiary

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class EmotionDiary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    private String emotion;

    private int score;

    private String content;
}
