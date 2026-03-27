package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.entity.Tag;
import com.blog.service.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TagController {
    private final TagService tagService;

    @GetMapping("/api/tags")
    public ApiResponse<?> listPublic() {
        return ApiResponse.success(tagService.listPublic());
    }

    @GetMapping("/api/admin/tags")
    public ApiResponse<?> listAdmin() {
        return ApiResponse.success(tagService.listAdmin());
    }

    @PostMapping("/api/admin/tags")
    public ApiResponse<?> create(@RequestBody Tag tag) {
        return ApiResponse.success(tagService.create(tag));
    }

    @PutMapping("/api/admin/tags/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody Tag tag) {
        return ApiResponse.success(tagService.update(id, tag));
    }

    @DeleteMapping("/api/admin/tags/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        tagService.delete(id);
        return ApiResponse.success();
    }
}
