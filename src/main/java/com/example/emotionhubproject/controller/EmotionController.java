package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.entity.EmotionDiary;
import com.example.emotionhubproject.repository.EmotionDiaryRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
    private EmotionDiaryRepository repository;

    /*
    메인 화면
    */
    @GetMapping("/")
    public String getHome(Model model) {
        model.addAttribute("pageTitle", "오늘의 감정 기록");
        //로그아웃 시 세션 제거 확인
        //HttpSession session = request.getSession(false);
        //System.out.println(session);

        List<EmotionDiary> diaryList =
                repository.findAll();

        model.addAttribute(
                "diaryList",
                diaryList
        );



    /*
    추천 문장
    */

        String recommendMessage =
                "오늘의 감정을 기록해보세요.";

        if(!diaryList.isEmpty()) {

            EmotionDiary latestDiary =
                    diaryList.get(
                            diaryList.size() - 1
                    );

            String emotion =
                    latestDiary.getEmotion();

            if(emotion.equals("행복")) {
                recommendMessage =
                        "행복한 하루네요!!☀️";
            }

            else if(emotion.equals("신남")) {
                recommendMessage =
                        "신나는 에너지가 느껴져요~🎵";
            }

            else if(emotion.equals("평범")) {
                recommendMessage =
                        "평온한 하루였네요 🌿";
            }

            else if(emotion.equals("우울")) {
                recommendMessage =
                        "오늘은 조금 쉬어가도 괜찮아요 ☁️";
            }

            else if(emotion.equals("분노")) {
                recommendMessage =
                        "화난 마음을 천천히 가라앉혀보세요 🔥";
            }

            else if(emotion.equals("기쁨")) {
                recommendMessage =
                        "기쁜 감정이 가득하네요 😊";
            }

            else if(emotion.equals("화남")) {
                recommendMessage =
                        "답답한 마음이 느껴져요 😢";
            }
        }

        model.addAttribute(
                "recommendMessage",
                recommendMessage
        );

        //최근 7일 감정 흐름


        String flowMessage = "";

        for(int i = 0; i < diaryList.size(); i++) {

            EmotionDiary diary =
                    diaryList.get(i);

            flowMessage +=
                    diary.getScore();

            if(i != diaryList.size() - 1) {
                flowMessage += " → ";
            }
        }

        model.addAttribute(
                "flowMessage",
                flowMessage
        );


        //감정 흐름 분석


        String flowResult =
                "감정 흐름을 분석중입니다.";

        if(diaryList.size() >= 2) {

            int firstScore =
                    diaryList.get(0).getScore();

            int lastScore =
                    diaryList.get(
                            diaryList.size() - 1
                    ).getScore();

            //전체적으로 상승


            if(lastScore > firstScore) {

                flowResult =
                        "최근 감정이 점점 좋아지고 있어요 🌱";
            }

            //전체적으로 하락

            else if(lastScore < firstScore) {

                flowResult =
                        "최근 감정이 조금 지쳐 보입니다 ☁️";
            }

            //높은 점수 유지

            else {

                boolean highMood = true;

                for(EmotionDiary diary : diaryList) {

                    if(diary.getScore() < 1) {
                        highMood = false;
                    }
                }

                if(highMood) {

                    flowResult =
                            "좋은 감정 상태를 계속 유지하고 있어요 ☀️";
                }


                //낮은 점수 유지

                boolean lowMood = true;

                for(EmotionDiary diary : diaryList) {

                    if(diary.getScore() > 0) {
                        lowMood = false;
                    }
                }

                if(lowMood) {

                    flowResult =
                            "많이 지쳐있는 상태로 보여요 😢";
                }
            }
        }

        model.addAttribute(
                "flowResult",
                flowResult
        );

        //가장 많이 나온 감정

        int happyCount = 0;
        int excitingCount = 0;
        int normalCount = 0;
        int sadCount = 0;
        int angryCount = 0;
        int joyCount = 0;
        int madCount = 0;

        for(EmotionDiary diary : diaryList) {

            String emotion =
                    diary.getEmotion();

            if(emotion.equals("행복")) {
                happyCount++;
            }

            else if(emotion.equals("신남")) {
                excitingCount++;
            }

            else if(emotion.equals("평범")) {
                normalCount++;
            }

            else if(emotion.equals("우울")) {
                sadCount++;
            }

            else if(emotion.equals("분노")) {
                angryCount++;
            }

            else if(emotion.equals("기쁨")) {
                joyCount++;
            }

            else if(emotion.equals("화남")) {
                madCount++;
            }
        }

        int max =
                happyCount;

        String topEmotion =
                "행복";

        if(excitingCount > max) {
            max = excitingCount;
            topEmotion = "신남";
        }

        if(normalCount > max) {
            max = normalCount;
            topEmotion = "평범";
        }

        if(sadCount > max) {
            max = sadCount;
            topEmotion = "우울";
        }

        if(angryCount > max) {
            max = angryCount;
            topEmotion = "분노";
        }

        if(joyCount > max) {
            max = joyCount;
            topEmotion = "기쁨";
        }

        if(madCount > max) {
            max = madCount;
            topEmotion = "화남";
        }

        model.addAttribute(
                "topEmotion",
                topEmotion
        );



        return "home";
    }

    // 저장
    @PostMapping("/save")
    public String save(
            @ModelAttribute EmotionDiary diary) {

        String emotion =
                diary.getEmotion();

        int score =
                calculateEmotionScore(emotion);

        diary.setScore(score);

        repository.save(diary);



    /*
    7개 초과 시
    가장 오래된 데이터 삭제
    */

        List<EmotionDiary> diaryList =
                repository.findAll();

        if(diaryList.size() > 7) {

            EmotionDiary oldestDiary =
                    diaryList.get(0);

            repository.delete(oldestDiary);
        }

        return "redirect:/";
    }

    //감정 점수 계산

    public int calculateEmotionScore(String emotion) {

        if(emotion.equals("행복")) {
            return 3;
        }

        if(emotion.equals("기쁨")) {
            return 2;
        }

        if(emotion.equals("신남")) {
            return 1;
        }

        if(emotion.equals("평범")) {
            return 0;
        }

        if(emotion.equals("우울")) {
            return -1;
        }

        if(emotion.equals("화남")) {
            return -2;
        }

        if(emotion.equals("분노")) {
            return -3;
        }

        return 0;
    }

}
