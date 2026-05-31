package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.dto.CommentResponse;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.ArticleService;
import com.example.emotionhubproject.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final CommentService commentService;

    // 목록
    @GetMapping({""})
    public String index(Model model) {
        List<Article> articleList = articleService.getAllArticles();

        model.addAttribute("pageTitle", "Community");
        model.addAttribute("articleList", articleList);
        model.addAttribute("keyword", "");
        model.addAttribute("isTitle", true);
        model.addAttribute("isContent", false);
        return "articles/index";
    }
    //글쓰기
    @GetMapping("/post")
    public String newArticle(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {

        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/articles";
        }

        model.addAttribute("pageTitle","Create Articles");
        return "articles/new";
    }
    //글 DB저장
    @PostMapping("/post")
    public String create(Model model, ArticleForm articleForm, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/articles";
        }

        try {
            articleService.createArticle(articleForm, loginUser);
            model.addAttribute("pageTitle", "Community");
            return "redirect:/articles";

        } catch (ErrorMessageException e) {
            model.addAttribute("pageTitle", "글쓰기");
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/post";
        }
    }

    //게시물 하나 조회하기
    @GetMapping("/{id}")
    public String show(HttpServletRequest request, @PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){
        try {
            UserEntity loginUser = getLoginUser(request);
            boolean loggedIn = loginUser != null;

            //article
            Article article = articleService.getArticle(id);
            model.addAttribute("pageTitle",article.getTitle());
            model.addAttribute("article", article);
            boolean isOwner = loginUser != null && article.isOwner(loginUser.getId());
            model.addAttribute("isOwner", isOwner);

            //comment
            List<CommentResponse> comments = commentService.getComments(id, loginUser);
            model.addAttribute("comments", comments);
            model.addAttribute("loggedIn", loggedIn);

            return "articles/show";
        } catch (ErrorMessageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles";
        }
    }

    //게시물 수정
    @GetMapping("/{id}/edit")
    public String editForm(HttpServletRequest request, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes){

        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/articles";
        }

        try {
            Article article = articleService.getArticle(id);
            //본인게시글 접근 제한
            if (!article.isOwner(loginUser.getId())) {
                redirectAttributes.addFlashAttribute("errorMessage", "Not authorized");
                return "redirect:/articles/"+id;
            }
            model.addAttribute("pageTitle", "Edit: " + article.getTitle());
            model.addAttribute("article", article);
            return "articles/edit";
        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }
    }
    @PostMapping("/{id}/edit")
    public String update(@PathVariable Long id, HttpServletRequest request, ArticleUpdateForm articleUpdateForm, RedirectAttributes redirectAttributes){
        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not authorized");
            return "redirect:/articles/"+id;
        }

        try {
            Article article = articleService.updateArticle(articleUpdateForm, loginUser);
            return "redirect:/articles/" + article.getId();

        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {

        UserEntity loginUser = getLoginUser(request);
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "권한이 없습니다.");
            return "redirect:/articles";
        }

        try {
            articleService.deleteArticle(id, loginUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "삭제되었습니다.");
            return "redirect:/articles";
        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }

    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false) String type) {
        List<Article> articleList = articleService.searchArticles(keyword,type);
        model.addAttribute("pageTitle","search");
        model.addAttribute("isTitle", "title".equals(type));
        model.addAttribute("isContent", "content".equals(type));
        model.addAttribute("articleList", articleList);
        model.addAttribute("keyword", keyword != null ? keyword : "검색어를 입력"); // 검색창에 검색어 유지용

        return "articles/index";
    }

    private UserEntity getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (UserEntity) session.getAttribute("user");
    }



}
