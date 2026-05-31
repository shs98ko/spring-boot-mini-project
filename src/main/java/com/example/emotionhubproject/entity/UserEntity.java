package com.example.emotionhubproject.entity;

import com.example.emotionhubproject.dto.ChangePasswordForm;
import com.example.emotionhubproject.dto.UserUpdateForm;
import com.example.emotionhubproject.exception.ErrorMessageException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
@Getter
@ToString
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name, username, email, password;

    public UserEntity(String name, String username, String email, String password) {
        this.name = name;
        this.username =username;
        this.email = email;
        this.password= password;
    }
    public void logInfo(){
        log.info("User=> id: {}, name: {},username:{}, email:{}, password:{}",id, name,username, email, password);}

    public void updateUser(UserUpdateForm dto){
        if(dto.getName() != null){
            this.name = dto.getName();
        }
        if(dto.getEmail() != null){
            this.email = dto.getEmail();
        }
        if(dto.getUsername() != null){
            this.username = dto.getUsername();
        }
    }

    public  void changePassword(ChangePasswordForm dto){
        if(!this.password.equals(dto.getOldPassword())){
            throw new ErrorMessageException("현재 비밀번호가 일치하지 않습니다.");
        }
        if(this.password.equals(dto.getNewPassword())){
            throw new ErrorMessageException("새로운 비밀번호를 입력해주세요.");
        }
        if(!dto.getNewPassword().equals(dto.getNewPasswordConfirmation())){
            throw new ErrorMessageException("새 비밀번호 확인이 일치하지 않습니다.");
        }
        this.password = dto.getNewPassword();
    }
}
