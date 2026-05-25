package com.example.emotionhubproject.api;

import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.repository.EmotionDiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/emotions")
public class EmotionApiController {
    @Autowired
    private EmotionDiaryRepository repository;

    /*
    전체 감정 조회 API
    */
    @GetMapping
    public List<EmotionDiary> list() {

        return repository.findAll();
    }
}
