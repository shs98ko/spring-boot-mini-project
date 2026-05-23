package com.example.emotionhubproject.repository;

import com.example.emotionhubproject.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {
    boolean existsByEmailOrUsername(String email, String username);
    UserEntity findByUsername(String username);

}
