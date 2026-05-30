package com.example.emotionhubproject.service;

import com.example.emotionhubproject.entity.EmotionDiary;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionAnalysisServiceImpl implements EmotionAnalysisService {

    // 추천 문장
    public String recommendMessage(List<EmotionDiary> diaryList) {
        if (diaryList.isEmpty()) return "오늘의 감정을 기록해보세요.";

        String emotion = diaryList.get(diaryList.size() - 1).getEmotion();

        switch (emotion) {
            case "행복": return "행복한 하루네요!!☀️";
            case "신남": return "신나는 에너지가 느껴져요~🎵";
            case "평범": return "평온한 하루였네요 🌿";
            case "우울": return "오늘은 조금 쉬어가도 괜찮아요 ☁️";
            case "분노": return "화난 마음을 천천히 가라앉혀보세요 🔥";
            case "기쁨": return "기쁜 감정이 가득하네요 😊";
            case "화남": return "답답한 마음이 느껴져요 😢";
            default: return "오늘의 감정을 기록해보세요.";
        }
    }

    // 감정 흐름
    public String flowMessage(List<EmotionDiary> diaryList) {
        if (diaryList.isEmpty()) return "";

        StringBuilder flowMessage = new StringBuilder();
        for (int i = 0; i < diaryList.size(); i++) {
            flowMessage.append(diaryList.get(i).getScore());
            if (i != diaryList.size() - 1) flowMessage.append(" → ");
        }
        return flowMessage.toString();
    }

    // 감정 흐름 분석
    public String flowResult(List<EmotionDiary> diaryList) {
        if (diaryList.size() < 2) return "감정 흐름을 분석중입니다.";

        int firstScore = diaryList.get(0).getScore();
        int lastScore = diaryList.get(diaryList.size() - 1).getScore();

        if (lastScore > firstScore) return "최근 감정이 점점 좋아지고 있어요 🌱";
        if (lastScore < firstScore) return "최근 감정이 조금 지쳐 보입니다 ☁️";
        return "감정이 비슷한 상태를 유지하고 있어요.";
    }

    // 가장 많이 나온 감정
    public String topEmotion(List<EmotionDiary> diaryList) {
        if (diaryList.isEmpty()) return "아직 기록된 감정이 없어요.";

        int happyCount = 0, excitingCount = 0, normalCount = 0;
        int sadCount = 0, angryCount = 0, joyCount = 0, madCount = 0;

        for (EmotionDiary diary : diaryList) {
            String emotion = diary.getEmotion();
            if (emotion.equals("행복")) happyCount++;
            else if (emotion.equals("신남")) excitingCount++;
            else if (emotion.equals("평범")) normalCount++;
            else if (emotion.equals("우울")) sadCount++;
            else if (emotion.equals("분노")) angryCount++;
            else if (emotion.equals("기쁨")) joyCount++;
            else if (emotion.equals("화남")) madCount++;
        }

        int max = happyCount;
        String topEmotion = "행복";

        if (excitingCount > max) {
            max = excitingCount;
            topEmotion = "신남";
        }
        if (normalCount > max)   {
            max = normalCount;
            topEmotion = "평범";
        }
        if (sadCount > max)      {
            max = sadCount;
            topEmotion = "우울";
        }
        if (angryCount > max)    {
            max = angryCount;
            topEmotion = "분노";
        }
        if (joyCount > max)      {
            max = joyCount;
            topEmotion = "기쁨";
        }
        if (madCount > max)      {
            topEmotion = "화남";
        }

        return topEmotion;
    }
}