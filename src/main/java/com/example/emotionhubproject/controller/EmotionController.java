package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.service.EmotionAnalysisService;
import com.example.emotionhubproject.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmotionController {

    @Autowired
    private EmotionService emotionService;

    @Autowired
    private EmotionAnalysisService emotionAnalysisService;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("pageTitle", "오늘의 감정 기록");

        List<EmotionDiary> diaryList = emotionService.getDiariesList();
        model.addAttribute("diaryList", diaryList);

        model.addAttribute("recommendMessage", emotionAnalysisService.recommendMessage(diaryList));
        model.addAttribute("flowMessage", emotionAnalysisService.flowMessage(diaryList));
        model.addAttribute("flowResult", emotionAnalysisService.flowResult(diaryList));
        model.addAttribute("showTopEmotion", diaryList.size() >= 3);
        model.addAttribute("topEmotion", emotionAnalysisService.topEmotion(diaryList));

        return "home";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute EmotionDiary diary) {
        emotionService.save(diary);
        emotionService.deleteOldest();
        return "redirect:/";
    }
}