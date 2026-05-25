package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@NoArgsConstructor
@Setter
@AllArgsConstructor
@ToString
@Slf4j
public class EmotionSaveRequest {
    private String emotion;
    private String content;
}
