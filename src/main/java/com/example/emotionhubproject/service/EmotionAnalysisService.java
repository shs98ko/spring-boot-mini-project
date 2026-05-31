package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.EmotionDiary;

import java.util.List;

public interface EmotionAnalysisService  {
    String getRecommendMessage(List<EmotionDiary> diaryList);
    String getFlowMessage(List<EmotionDiary> diaryList);
    String getFlowResult(List<EmotionDiary> diaryList);
    String getTopEmotion(List<EmotionDiary> diaryList);

}