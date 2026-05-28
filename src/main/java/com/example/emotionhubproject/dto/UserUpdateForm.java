package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class UserUpdateForm {
    private String name;
    private String email;
    private String username;
}
