package com.example.emotionhubproject.repository;

import com.example.emotionhubproject.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    boolean existsBy(String email, String username);
}
