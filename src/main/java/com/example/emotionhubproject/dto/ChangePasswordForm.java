package com.example.emotionhubproject.dto;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Slf4j
public class ChangePasswordForm {
    private String  oldPassword, newPassword, newPasswordConfirmation;
}
