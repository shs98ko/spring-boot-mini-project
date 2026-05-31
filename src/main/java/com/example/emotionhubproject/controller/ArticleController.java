package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateForm;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.service.ArticleServiceImpl;
import com.example.emotionhubproject.service.CommentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleServiceImpl articleServiceImpl;
    private final CommentServiceImpl commentServiceImpl;

    // 목록
    @GetMapping({""})
    public String index(Model model) {
        List<Article> articleList = articleServiceImpl.getAllArticles();

        model.addAttribute("pageTitle", "Articles");
        model.addAttribute("articleList", articleList);
        model.addAttribute("keyword", "");
        model.addAttribute("isTitle", true);
        model.addAttribute("isContent", false);
        return "articles/index";
    }
    //글쓰기
    @GetMapping("/post")
    public String getNewArticle(Model model, HttpServletRequest request,RedirectAttributes redirectAttributes) {

        HttpSession session =request.getSession(false);
        //세션 먼저 체크 (로그인 하지 않고 접속 하는 경우 대비)
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        if(loginUser==null){
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";

        }

        model.addAttribute("pageTitle","Create Articles");
        return "articles/new";
    }
    //글 DB저장
    @PostMapping("/post")
    public String postNewArticle(Model model, ArticleForm articleForm, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session =request.getSession(false);
        //세션 먼저 체크 (로그인 안한경우)
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        if(loginUser==null){
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }

        try {
            articleServiceImpl.createArticle(articleForm, loginUser);
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
    public String showArticle(HttpServletRequest request, @PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){
        try {
            HttpSession session = request.getSession(false); // 세션 없으면 null 반환 (새로 만들지 않음)
            UserEntity loginUser = session != null ? (UserEntity) session.getAttribute("user") : null; // 세션 null이면 loginUser도 null
            boolean loggedIn = loginUser != null; // false

            //article
            Article article = articleServiceImpl.getArticle(id);
            model.addAttribute("pageTitle",article.getTitle());
            model.addAttribute("article", article);
            boolean isOwner = loginUser != null && articleServiceImpl.isOwner(article, loginUser.getId());
            model.addAttribute("isOwner", isOwner);

            //comment
            List<Map<String,Object>> comments = commentServiceImpl.getComments(id, loginUser);
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
    public String getEdit(HttpServletRequest request, @PathVariable Long id, Model model, RedirectAttributes redirectAttributes){

        HttpSession session = request.getSession(false);
        //세션 먼저 체크 (로그인 하지 않고 접속 하는 경우 대비)
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        //로그인 접근 제한
        if (loginUser == null){
            return "redirect:/articles";
        }

        try {
            Article article = articleServiceImpl.getArticle(id);
            //본인게시글 접근 제한
            if (!articleServiceImpl.isOwner(article, loginUser.getId())) {
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
    public String postEdit(@PathVariable Long id, HttpServletRequest request, ArticleUpdateForm articleUpdateForm, RedirectAttributes redirectAttributes){
        HttpSession session = request.getSession(false);
        //세션 먼저 체크 (로그인 하지 않고 접속 하는 경우 대비)
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        //로그인 접근 제한
        if (loginUser == null){
            return "redirect:/articles/"+id;
        }
        try {
            Article article = articleServiceImpl.updateArticle(articleUpdateForm, loginUser);
            return "redirect:/articles/" + article.getId();

        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }
    }


    @GetMapping("/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        //세션 먼저 체크 (로그인 하지 않고 접속 하는 경우 대비)
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage","Not authorized");
            return "redirect:/articles";
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");

        //로그인 접근 제한
        if (loginUser == null){
            return "redirect:/articles";
        }

        try {
            articleServiceImpl.deleteArticle(id, loginUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "삭제되었습니다.");
            return "redirect:/articles";
        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }

    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(required = false) String keyword, @RequestParam(required = false) String type) {
        List<Article> articleList = articleServiceImpl.searchArticles(keyword,type);
        model.addAttribute("pageTitle","search");
        model.addAttribute("isTitle", "title".equals(type));
        model.addAttribute("isContent", "content".equals(type));
        model.addAttribute("articleList", articleList);
        model.addAttribute("keyword", keyword != null ? keyword : "검색어를 입력"); // 검색창에 검색어 유지용

        return "articles/index";
    }
    /*
    private boolean isNotLoggedIn(HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession(false);
        if (session == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not authorized");
            return true;
        }
        UserEntity loginUser = (UserEntity) session.getAttribute("user");
        if (loginUser == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Not authorized");
            return true;
        }
        return false;
    }
    private UserEntity getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) return null;
        return (UserEntity) session.getAttribute("user");
    }
    */


}
