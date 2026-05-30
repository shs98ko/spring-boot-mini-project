package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.EmotionDiary;

import java.util.List;

public interface EmotionAnalysisService  {
    String recommendMessage(List<EmotionDiary> diaryList);
    String flowMessage(List<EmotionDiary> diaryList);
    String flowResult(List<EmotionDiary> diaryList);
    String topEmotion(List<EmotionDiary> diaryList);

}