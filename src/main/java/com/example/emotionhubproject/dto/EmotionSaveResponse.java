package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class EmotionSaveResponse {
    private String recommendMessage;
    private String flowMessage;
    private String flowResult;
    private int count;
}