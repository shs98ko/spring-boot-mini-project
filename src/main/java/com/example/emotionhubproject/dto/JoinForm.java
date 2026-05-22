package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
@ToString@Slf4j
public class JoinForm {
    private String  name,username, email, password, password2;
    public void logInfo() {
        log.info("Join Form => name: {},username:{}, email:{}, password:{}, password2:{}", name,username, email, password, password2);
    }

}
