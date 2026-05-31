package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.EmotionDiaryForm;
import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.EmotionAnalysisServiceImpl;
import com.example.emotionhubproject.service.EmotionServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class EmotionController {

    private final EmotionServiceImpl emotionServiceImpl;
    private final EmotionAnalysisServiceImpl emotionAnalysisServiceImpl;

    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("pageTitle", "오늘의 감정 기록");

        List<EmotionDiary> diaryList = emotionServiceImpl.getDiaries();
        model.addAttribute("diaryList", diaryList);

        model.addAttribute("recommendMessage", emotionAnalysisServiceImpl.getRecommendMessage(diaryList));
        model.addAttribute("flowMessage", emotionAnalysisServiceImpl.getFlowMessage(diaryList));
        model.addAttribute("flowResult", emotionAnalysisServiceImpl.getFlowResult(diaryList));
        model.addAttribute("showTopEmotion", diaryList.size() >= 3);
        model.addAttribute("topEmotion", emotionAnalysisServiceImpl.getTopEmotion(diaryList));

        return "home";
    }

    @PostMapping("/save")
    public String save(EmotionDiaryForm emotionDiaryForm, RedirectAttributes redirectAttributes) {
        try{
            emotionServiceImpl.saveDiary(emotionDiaryForm);
            return "redirect:/";}
        catch (ErrorMessageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/";
        }
    }
}