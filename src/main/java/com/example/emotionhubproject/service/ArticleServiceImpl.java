package com.example.emotionhubproject.service;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateForm;
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
public class ArticleServiceImpl implements ArticleService {
    private final ArticleRepository articleRepository;

    //게시물 전제 조회
    public List<Article> getAllArticles() {
        return articleRepository.findAll();
    }

    //게시물 조회
    public Article getArticle(Long id){
        return articleRepository.findById(id).orElseThrow(() -> new ErrorMessageException("Article not found."));
    }

    public Article updateArticle(ArticleUpdateForm articleUpdateForm, UserEntity user){

        if (articleUpdateForm.getTitle() == null || articleUpdateForm.getTitle().trim().isEmpty()) {
            throw new ErrorMessageException("제목을 입력해주세요.");
        }
        if (articleUpdateForm.getContent() == null || articleUpdateForm.getContent().trim().isEmpty()) {
            throw new ErrorMessageException("내용을 입력해주세요.");
        }
        Long updateFormId= articleUpdateForm.getId();

        //게시글 조회
        Article article = articleRepository.findById(updateFormId)
                .orElseThrow(()-> new ErrorMessageException("Article not found"));

        //본인확인
        if(!article.getUserId().equals(user.getId())){
            throw new ErrorMessageException("Not authorized");
        }

        article.patch(articleUpdateForm);
        return articleRepository.save(article);
    }

    public void createArticle(ArticleForm articleForm, UserEntity user){
        if (articleForm.getTitle() == null || articleForm.getTitle().trim().isEmpty()) {
            throw new ErrorMessageException("제목을 입력해주세요.");
        }
        if (articleForm.getContent() == null || articleForm.getContent().trim().isEmpty()) {
            throw new ErrorMessageException("내용을 입력해주세요.");
        }
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
    public List<Article> searchArticles(String keyword, String type) {
        if (keyword == null || keyword.isBlank()) {
            return getAllArticles();
        }
        if(type.equals("title")){
            return articleRepository.findByTitleContaining(keyword);
        } else {
            return articleRepository.findByContentContaining(keyword);
        }
    }
}
