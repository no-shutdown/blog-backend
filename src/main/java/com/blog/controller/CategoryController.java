package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.entity.Category;
import com.blog.service.CategoryService;
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
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/api/categories")
    public ApiResponse<?> listPublic() {
        return ApiResponse.success(categoryService.listPublic());
    }

    @GetMapping("/api/admin/categories")
    public ApiResponse<?> listAdmin() {
        return ApiResponse.success(categoryService.listAdmin());
    }

    @PostMapping("/api/admin/categories")
    public ApiResponse<?> create(@RequestBody Category category) {
        return ApiResponse.success(categoryService.create(category));
    }

    @PutMapping("/api/admin/categories/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody Category category) {
        return ApiResponse.success(categoryService.update(id, category));
    }

    @DeleteMapping("/api/admin/categories/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        categoryService.delete(id);
        return ApiResponse.success();
    }
}
