package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter@Setter
@AllArgsConstructor@NoArgsConstructor
@ToString
public class JoinForm {
    private String  name,username, email, password, password2;

}
