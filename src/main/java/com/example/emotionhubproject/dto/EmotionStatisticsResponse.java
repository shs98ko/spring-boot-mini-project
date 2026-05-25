package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class EmotionStatisticsResponse {
    private String topEmotion;
    private int count;
}