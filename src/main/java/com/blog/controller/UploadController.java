package com.blog.controller;

import com.blog.dto.ApiResponse;
import com.blog.service.UploadService;
import java.io.IOException;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/upload")
@RequiredArgsConstructor
public class UploadController {
    private final UploadService uploadService;

    @PostMapping("/image")
    public ApiResponse<?> upload(@RequestParam("file") MultipartFile file) throws IOException {
        return ApiResponse.success(Map.of("url", uploadService.uploadImage(file)));
    }
}

