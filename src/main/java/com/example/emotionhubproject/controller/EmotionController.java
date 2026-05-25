package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.repository.EmotionDiaryRepository;
import com.example.emotionhubproject.service.EmotionService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    /*
    메인 화면
    */
    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("pageTitle", "오늘의 감정 기록");

        //로그아웃 시 세션 제거 확인
        //HttpSession session = request.getSession(false);
        //System.out.println(session);

        List<EmotionDiary> diaryList = emotionService.getDiariesList();
        model.addAttribute("diaryList", diaryList);

        //추천 문장
        String recommendMessage = emotionService.recommendMessage(diaryList);
        model.addAttribute("recommendMessage", recommendMessage);

        //최근 7일 감정 흐름
        String flowMessage = emotionService.flowMessage(diaryList);
        model.addAttribute("flowMessage", flowMessage);

        //감정 흐름 분석
        String flowResult = emotionService.flowResult(diaryList);
        model.addAttribute("flowResult", flowResult);

        //가장 많이 나온 감정

        String topEmotion = emotionService.topEmotion(diaryList);
        model.addAttribute("topEmotion", topEmotion);

        return "home";
    }

    // 저장
    @PostMapping("/save")
    public String save(@ModelAttribute EmotionDiary diary) {
        List<EmotionDiary> diaryList = emotionService.getDiariesList();
        emotionService.save(diary);
        emotionService.delete(diaryList);
        return "redirect:/";
    }
}