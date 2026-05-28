package com.example.emotionhubproject.entity;

import com.example.emotionhubproject.dto.UserUpdateForm;
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
    Long id;
    String name, username, email, password;

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
}
