package com.example.emotionhubproject.controller;

import com.example.emotionhubproject.dto.ArticleForm;
import com.example.emotionhubproject.dto.ArticleUpdateDto;
import com.example.emotionhubproject.entity.Article;
import com.example.emotionhubproject.entity.UserEntity;
import com.example.emotionhubproject.exception.ErrorMessageException;
import com.example.emotionhubproject.repository.ArticleRepository;
import com.example.emotionhubproject.service.ArticleService;
import com.example.emotionhubproject.service.CommentService;
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
import java.util.Map;
import java.util.Objects;

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

        model.addAttribute("pageTitle", "Articles");
        model.addAttribute("articleList", articleList);
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
    public String postNewArticle(ArticleForm articleForm, HttpServletRequest request, RedirectAttributes redirectAttributes) {

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

        articleService.postArticle(articleForm, loginUser);
        return "redirect:/articles";
    }

    //게시물 하나 조회하기
    @GetMapping("/{id}")
    public String showArticle(HttpServletRequest request, @PathVariable Long id ,Model model, RedirectAttributes redirectAttributes){
        try {
            HttpSession session = request.getSession(false); // 세션 없으면 null 반환 (새로 만들지 않음)
            UserEntity loginUser = session != null ? (UserEntity) session.getAttribute("user") : null; // 세션 null이면 loginUser도 null
            boolean loggedIn = loginUser != null; // false

            //article
            Article article = articleService.getArticle(id);
            model.addAttribute("pageTitle",article.getTitle());
            model.addAttribute("article", article);
            boolean isOwner = loginUser != null && articleService.isOwner(article, loginUser.getId());
            model.addAttribute("isOwner", isOwner);

            //comment
            List<Map<String,Object>> comments = commentService.getComments(id, loginUser);
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
            Article article = articleService.getArticle(id);
            //본인게시글 접근 제한
            if (!articleService.isOwner(article, loginUser.getId())) {
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
    public String postEdit(@PathVariable Long id,HttpServletRequest request,ArticleUpdateDto articleUpdateDto,RedirectAttributes redirectAttributes){
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
            Article article = articleService.getUpdateArticle(articleUpdateDto, loginUser);
            return "redirect:/articles/" + article.getId();

        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("error", e.getMessage());
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
            articleService.deleteArticle(id, loginUser.getId());
            redirectAttributes.addFlashAttribute("successMessage", "삭제되었습니다.");
            return "redirect:/articles";
        }catch (ErrorMessageException e){
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/articles/"+id;
        }

    }

}
