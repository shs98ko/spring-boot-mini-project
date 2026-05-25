package com.example.emotionhubproject.repository;


import com.example.emotionhubproject.entity.EmotionDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionDiaryRepository
        extends JpaRepository<EmotionDiary, Long> {
}
