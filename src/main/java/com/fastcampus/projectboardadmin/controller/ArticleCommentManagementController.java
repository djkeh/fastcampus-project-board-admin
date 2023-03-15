package com.fastcampus.projectboardadmin.controller;

import com.fastcampus.projectboardadmin.dto.response.ArticleCommentResponse;
import com.fastcampus.projectboardadmin.service.ArticleCommentManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/management/article-comments")
@Controller
public class ArticleCommentManagementController {

    private final ArticleCommentManagementService articleCommentManagementService;

    @GetMapping
    public String articleComments(Model model) {
        model.addAttribute(
                "comments",
                articleCommentManagementService.getArticleComments().stream().map(ArticleCommentResponse::of).toList()
        );

        return "management/article-comments";
    }

    @ResponseBody
    @GetMapping("/{articleCommentId}")
    public ArticleCommentResponse articleComment(@PathVariable Long articleCommentId) {
        return ArticleCommentResponse.of(articleCommentManagementService.getArticleComment(articleCommentId));
    }

    @PostMapping("/{articleCommentId}")
    public String deleteArticleComment(@PathVariable Long articleCommentId) {
        articleCommentManagementService.deleteArticleComment(articleCommentId);

        return "redirect:/management/article-comments";
    }

}
