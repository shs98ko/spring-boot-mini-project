package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateDto;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    //게시물 전제 조회
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    //게시물 조회
    public Article getArticle(Long id){
        return articleRepository.findById(id).orElseThrow(() -> new ErrorMessageException("Article not found."));
    }
    //본인확인여부
    public boolean isOwner(Article article,Long userId){
        return article.getUserId().equals(userId);
    }

    public Article getUpdateArticle(ArticleUpdateDto articleUpdateDto, UserEntity user){
        Long updateFormId= articleUpdateDto.getId();

        //게시글 조회
        Article article = articleRepository.findById(updateFormId)
                .orElseThrow(()-> new ErrorMessageException("Article not found"));

        //본인확인
        if(!article.getUserId().equals(user.getId())){
            throw new ErrorMessageException("Not authorized");
        }

        article.patch(articleUpdateDto);
        return articleRepository.save(article);
    }

    public void postArticle(ArticleForm articleForm, UserEntity user){
        Article article = new Article(articleForm.getTitle(),articleForm.getContent(),user.getId(),user.getUsername());
        articleRepository.save(article);
    }

    public void deleteArticle(Long id, Long loginUserId) {
        //게시물 조회
        Article article = articleRepository.findById(id).orElseThrow(
                () -> new ErrorMessageException("Article not found."));
        //권한 제한
        if(!article.getUserId().equals(loginUserId)){
            throw new ErrorMessageException("Not authorized");
        }
        articleRepository.delete(article);
    }
}
