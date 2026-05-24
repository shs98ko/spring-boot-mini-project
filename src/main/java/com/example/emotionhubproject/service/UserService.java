package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.JoinForm;
import com.example.emotionhubproject.dto.LoginForm;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;

    public void join(JoinForm joinForm) {
        // 회원가입 로직

        //비밂번호 이중체크
        if(!joinForm.getPassword().equals(joinForm.getPassword2())){
            throw new ErrorMessageException("Password confirmation does not match.");
        }

        //ID와 email 존재 여부
        boolean exist = userRepository.existsByEmailOrUsername(joinForm.getEmail(), joinForm.getUsername());
        if(exist){
            throw new ErrorMessageException("This username/email is already taken.");
        }
        /*existsByUsernameOrEmail*/
        /*SELECT EXISTS(
            SELECT *
            FROM users
            WHERE username = ?
               OR email = ?
        )*/
        //DTO-> Entity로 변환 -> Repository를 통해 DB로 Entity를 저장
        UserEntity user   = new UserEntity(joinForm.getName(),joinForm.getUsername(),joinForm.getEmail(), joinForm.getPassword(),null);
        UserEntity save = userRepository.save(user);
        save.logInfo();
    }

    public UserEntity login(LoginForm loginForm) {
        // 로그인 로직
        UserEntity user = userRepository.findByUsername(loginForm.getUsername());
        //ID 존재여부 체크
        if(user == null){
            throw new ErrorMessageException("An account with username does not exist");
        }
        //비밀번호 존재 여부 체크
        if(!user.getPassword().equals(loginForm.getPassword())){
            throw new ErrorMessageException("Wrong password.");
        }
        user.logInfo();
        return user;
    }

}
