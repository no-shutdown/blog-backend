package com.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DashboardDto {
    private long articleCount;
    private long publishedCount;
    private long draftCount;
    private long commentCount;
    private long pendingCommentCount;
    private long totalViewCount;
}
