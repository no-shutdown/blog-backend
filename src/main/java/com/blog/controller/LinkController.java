package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.entity.Link;
import com.blog.service.LinkService;
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
public class LinkController {
    private final LinkService linkService;

    @GetMapping("/api/links")
    public ApiResponse<?> listPublic() {
        return ApiResponse.success(linkService.listVisible());
    }

    @GetMapping("/api/admin/links")
    public ApiResponse<?> listAdmin() {
        return ApiResponse.success(linkService.listAll());
    }

    @PostMapping("/api/admin/links")
    public ApiResponse<?> create(@RequestBody Link link) {
        return ApiResponse.success(linkService.create(link));
    }

    @PutMapping("/api/admin/links/{id}")
    public ApiResponse<?> update(@PathVariable Long id, @RequestBody Link link) {
        return ApiResponse.success(linkService.update(id, link));
    }

    @DeleteMapping("/api/admin/links/{id}")
    public ApiResponse<?> delete(@PathVariable Long id) {
        linkService.delete(id);
        return ApiResponse.success();
    }
}
