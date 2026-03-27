package com.blog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CommentRequest {
    @NotNull
    private Long articleId;

    @NotBlank
    @Size(min = 1, max = 50)
    private String nickname;

    @Email
    private String email;

    @NotBlank
    @Size(min = 1, max = 1000)
    private String content;

    private Long parentId;
}
