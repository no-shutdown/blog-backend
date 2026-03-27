package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.dto.DashboardDto;
import com.blog.entity.Article;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentRepository;
import com.blog.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;
    private final CommentService commentService;

    @GetMapping("/dashboard")
    public ApiResponse<DashboardDto> dashboard() {
        long articleCount = articleRepository.count();
        long publishedCount = articleRepository.countByStatus(Article.ArticleStatus.published);
        long draftCount = articleRepository.countByStatus(Article.ArticleStatus.draft);
        long commentCount = commentRepository.count();
        long pendingCommentCount = commentService.countPending();
        long totalViewCount = articleRepository.sumViewCount();
        DashboardDto dto = new DashboardDto(articleCount, publishedCount, draftCount, commentCount, pendingCommentCount, totalViewCount);
        return ApiResponse.success(dto);
    }
}
