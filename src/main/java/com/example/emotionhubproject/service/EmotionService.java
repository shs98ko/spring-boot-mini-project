package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.EmotionDiary;

import java.util.List;

public interface EmotionService {
    List<EmotionDiary> getDiariesList();

    void save(EmotionDiary diary);

    void deleteOldest();
}

