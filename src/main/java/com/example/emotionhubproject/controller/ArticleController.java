package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateDto;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.service.ArticleService;
import com.example.emotionhubproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;
    @Autowired
    private ArticleRepository articleRepository;

    // 목록
    @GetMapping({""})
    public String index(Model model) {
        List<Article> articleList = articleService.getArticles();

        model.addAttribute("pageTitle", "Articles");
        model.addAttribute("articleList", articleList);

        return "articles/index";
    }
    //글쓰기
    @GetMapping("/post")
    public String getPost(Model model) {
        model.addAttribute("pageTitle","Create Articles");
        return "articles/new";
    }
    //글 DB저장
    @PostMapping("/post")
    public String postUpload(ArticleForm form) {
        Article article = form.toEntity();
        articleRepository.save(article);
        return "redirect:/articles";
    }

    //게시물 하나 조회하기
    @GetMapping("/{id}")
    public String showArticle(@PathVariable Long id ,Model model){
        try {
            Article article = articleService.getArticle(id);
            model.addAttribute("pageTitle",article.getTitle());
            model.addAttribute("article", article);
            return "articles/show";
        } catch (ErrorMessageException e) {
            model.addAttribute("error", e.getMessage());
            return "404";
        }
    }

    //게시물 수정
    @GetMapping("/{id}/edit")
    public String getEdit(HttpServletRequest request, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes){

        HttpSession session = request.getSession(false);
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        //로그인 접근 제한
        if (loginUser == null){
            return "redirect:/articles";
        }
        try {
            Article article = articleService.getArticle(id);
            //본인게시글 접근 제한
            if (!articleService.isOwner(article, loginUser.getId())) {
                model.addAttribute("errorMessage", "Not authorized");
                redirectAttributes.addFlashAttribute("errorMessage", "Not authorized");
                return "redirect:/articles";
            }
            model.addAttribute("pageTitle", "Edit: " + article.getTitle());
            model.addAttribute("article", article);
            return "/articles/edit";
        }catch (ErrorMessageException e){
            model.addAttribute("errorMessage", e.getMessage());
            return "404";
        }
    }
    @PostMapping("/{id}/edit")
    public String postEdit(HttpServletRequest request,ArticleUpdateDto articleUpdateDto,RedirectAttributes redirectAttributes){

        HttpSession session = request.getSession(false);
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        //로그인 접근 제한
        if (loginUser == null){
            return "redirect:/articles";
        }
        try {
            Article article = articleService.getUpdateArticle(articleUpdateDto, loginUser);
            return "redirect:/articles/" + article.getId();

        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/articles";
        }
    }


    @GetMapping("/{id}/delete")
    public String getRemove(@PathVariable Long id, Model model){
        Article article = articleRepository.findById(id).orElse(null);

        model.addAttribute("article", article);

        return "articles/index";
    }

}
