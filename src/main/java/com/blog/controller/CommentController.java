package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.dto.CommentRequest;
import com.blog.dto.CommentStatusRequest;
import com.blog.entity.Comment;
import com.blog.service.CommentService;
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
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/comments/{articleId}")
    public ApiResponse<?> list(@PathVariable Long articleId) {
        return ApiResponse.success(commentService.listApproved(articleId));
    }

    @PostMapping("/api/comments")
    public ApiResponse<?> submit(@Valid @RequestBody CommentRequest request) {
        return ApiResponse.success(commentService.submit(request));
    }

    @GetMapping("/api/admin/comments")
    public ApiResponse<?> adminList(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Comment.CommentStatus status,
            @RequestParam(required = false) Long articleId
    ) {
        return ApiResponse.success(commentService.listAdmin(page, size, status, articleId));
    }

    @PutMapping("/api/admin/comments/{id}")
    public ApiResponse<?> updateStatus(@PathVariable Long id, @Valid @RequestBody CommentStatusRequest request) {
        return ApiResponse.success(commentService.updateStatus(id, request.getStatus()));
    }

    @DeleteMapping("/api/admin/comments/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        commentService.delete(id);
        return ApiResponse.success();
    }
}
