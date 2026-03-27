package com.blog.dto;

import com.blog.entity.Article.ContentType;
import com.blog.entity.Article.ArticleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.Data;

@Data
public class ArticleRequest {
    @NotBlank
    @Size(max = 200)
    private String title;

    @NotBlank
    private String content;

    @NotNull
    private ContentType contentType;

    @Size(max = 500)
    private String summary;

    private String coverImage;

    @NotNull
    private ArticleStatus status;

    private Long categoryId;

    private List<Long> tagIds;
}
