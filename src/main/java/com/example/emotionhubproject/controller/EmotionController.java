package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.EmotionDiaryForm;
import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.EmotionAnalysisService;
import com.example.emotionhubproject.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionService emotionService;
    private final EmotionAnalysisService emotionAnalysisService;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("pageTitle", "오늘의 감정 기록");

        List<EmotionDiary> diaryList = emotionService.getDiaries();
        model.addAttribute("diaryList", diaryList);

        model.addAttribute("recommendMessage", emotionAnalysisService.getRecommendMessage(diaryList));
        model.addAttribute("flowMessage", emotionAnalysisService.getFlowMessage(diaryList));
        model.addAttribute("flowResult", emotionAnalysisService.getFlowResult(diaryList));
        model.addAttribute("showTopEmotion", diaryList.size() >= 3);
        model.addAttribute("topEmotion", emotionAnalysisService.getTopEmotion(diaryList));

        return "home";
    }

    @PostMapping("/save")
    public String save(EmotionDiaryForm emotionDiaryForm, RedirectAttributes redirectAttributes) {
        try{
            emotionService.saveDiary(emotionDiaryForm);
            return "redirect:/";}
        catch (ErrorMessageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }
}