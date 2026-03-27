package com.blog.dto;

import com.blog.entity.Comment.CommentStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CommentStatusRequest {
    @NotNull
    private CommentStatus status;
}
