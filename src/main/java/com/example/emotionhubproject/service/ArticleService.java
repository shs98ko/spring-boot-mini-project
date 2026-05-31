package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;

import java.util.List;

// 껍데기(메뉴판) 역할만 하는 인터페이스
public interface ArticleService {

    // 게시물 전체 조회
    List<Article> getAllArticles();

    // 단일 게시물 조회
    Article getArticle(Long id);

    // 본인 확인 여부
    boolean isOwner(Article article, Long userId);

    // 게시글 수정
    Article updateArticle(ArticleUpdateForm articleUpdateForm, UserEntity user);

    // 게시글 작성
    void createArticle(ArticleForm articleForm, UserEntity user);

    // 게시글 삭제
    void deleteArticle(Long id, Long loginUserId);

    List<Article> searchArticles(String keyword, String type);
}
