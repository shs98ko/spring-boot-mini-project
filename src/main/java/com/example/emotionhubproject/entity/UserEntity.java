package com.example.emotionhubproject.entity;

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
    Long articleId;

    public UserEntity(String name, String username, String email, String password, Long articleId) {
        this.name = name;
        this.username =username;
        this.email = email;
        this.password= password;
        this.articleId = articleId;
    }
    public void logInfo(){
        log.info("User=> id: {}, name: {},username:{}, email:{}, password:{}",id, name,username, email, password);}
}
