package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.EmotionDiaryForm;
import com.example.emotionhubproject.entity.EmotionDiary;

import java.util.List;

public interface EmotionService {
    List<EmotionDiary> getDiaries();

    void saveDiary(EmotionDiaryForm emotionDiaryForm);
}

