package com.blog.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Set;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class UploadService {
    private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    private static final Set<String> ALLOWED_EXTS = Set.of(".jpg", ".jpeg", ".png", ".gif", ".webp");

    @Value("${blog.upload.path}")
    private String uploadPath;

    public String uploadImage(MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if (file.getSize() > 5 * 1024 * 1024L) {
            throw new IllegalArgumentException("File is too large");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType)) {
            throw new IllegalArgumentException("Unsupported image type");
        }

        String originalName = file.getOriginalFilename();
        String ext = extractExt(originalName);
        if (!ALLOWED_EXTS.contains(ext)) {
            throw new IllegalArgumentException("Unsupported file extension");
        }

        Path dir = Paths.get(uploadPath);
        Files.createDirectories(dir);

        String filename = UUID.randomUUID() + ext;
        Path target = dir.resolve(filename).normalize();
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

        return "/uploads/images/" + filename;
    }

    private String extractExt(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "";
        }
        return filename.substring(filename.lastIndexOf('.')).toLowerCase();
    }
}
