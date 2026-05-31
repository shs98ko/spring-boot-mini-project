package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.EmotionDiaryForm;
import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.EmotionDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmotionServiceImpl implements EmotionService {

    @Autowired
    private EmotionDiaryRepository repository;

    // 전체 조회
    public List<EmotionDiary> getDiariesList() {
        return repository.findAll();
    }

    // 저장
    public void saveDiary(EmotionDiaryForm emotionDiaryForm) {
        int score = calculateEmotionScore(emotionDiaryForm.getEmotion());
        EmotionDiary emotionDiary = new EmotionDiary(emotionDiaryForm.getEmotion(), emotionDiaryForm.getContent(), score);
        repository.save(emotionDiary);

        // 7개 초과 시 가장 오래된 데이터 삭제
        List<EmotionDiary> diaryList = repository.findAll();
        if (diaryList.size() > 7) {
            repository.delete(diaryList.get(0));
        }
    }


    // 점수 계산
    private int calculateEmotionScore(String emotion) {
        switch (emotion) {
            case "행복": return 3;
            case "기쁨": return 2;
            case "신남": return 1;
            case "평범": return 0;
            case "우울": return -1;
            case "화남": return -2;
            case "분노": return -3;
            default: throw new ErrorMessageException(
                    "행복, 기쁨, 신남, 평범, 우울, 화남, 분노 중 하나만 입력 가능합니다.");
        }
    }
}