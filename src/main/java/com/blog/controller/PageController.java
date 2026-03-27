package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.entity.Page;
import com.blog.service.PageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PageController {
    private final PageService pageService;

    @GetMapping("/api/pages/{slug}")
    public ApiResponse<?> get(@PathVariable String slug) {
        return ApiResponse.success(pageService.getBySlug(slug));
    }

    @GetMapping("/api/admin/pages/{slug}")
    public ApiResponse<?> adminGet(@PathVariable String slug) {
        return ApiResponse.success(pageService.getBySlug(slug));
    }

    @PutMapping("/api/admin/pages/{slug}")
    public ApiResponse<?> update(@PathVariable String slug, @RequestBody Page page) {
        return ApiResponse.success(pageService.update(slug, page));
    }
}
