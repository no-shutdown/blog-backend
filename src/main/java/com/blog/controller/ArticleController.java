package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.dto.ArticleRequest;
import com.blog.entity.Article;
import com.blog.service.ArticleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ArticleController {
    private final ArticleService articleService;

    @GetMapping("/api/articles")
    public ApiResponse<?> listPublished(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) Long tagId,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.success(articleService.listPublished(page, size, categoryId, tagId, keyword));
    }

    @GetMapping("/api/articles/archive")
    public ApiResponse<?> archive() {
        return ApiResponse.success(articleService.archive());
    }

    @GetMapping("/api/articles/{id}")
    public ApiResponse<?> detail(@PathVariable Long id) {
        return ApiResponse.success(articleService.getPublishedDetail(id));
    }

    @GetMapping("/api/admin/articles")
    public ApiResponse<?> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Article.ArticleStatus status,
            @RequestParam(required = false) String keyword
    ) {
        return ApiResponse.success(articleService.listAdmin(page, size, status, keyword));
    }

    @GetMapping("/api/admin/articles/{id}")
    public ApiResponse<?> adminDetail(@PathVariable Long id) {
        return ApiResponse.success(articleService.getById(id));
    }

    @PostMapping("/api/admin/articles")
    public ApiResponse<?> create(@Valid @RequestBody ArticleRequest request) {
        return ApiResponse.success(articleService.create(request));
    }

    @PutMapping("/api/admin/articles/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @Valid @RequestBody ArticleRequest request) {
        return ApiResponse.success(articleService.update(id, request));
    }

    @DeleteMapping("/api/admin/articles/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        articleService.delete(id);
        return ApiResponse.success();
    }
}
