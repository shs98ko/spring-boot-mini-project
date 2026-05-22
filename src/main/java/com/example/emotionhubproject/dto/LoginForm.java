package com.example.emotionhubproject.dto;


import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class LoginForm {
    private String   email, password;
    public void logInfo() {
        log.info("Join Form =>  email:{}, password:{}", email, password);
    }
}
