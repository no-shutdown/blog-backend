package com.blog.service;

import com.blog.dto.CommentRequest;
import com.blog.dto.PageResult;
import com.blog.entity.Comment;
import com.blog.entity.Comment.CommentStatus;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CommentRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final ArticleRepository articleRepository;

    public Comment submit(CommentRequest req) {
        articleRepository.findByIdAndStatus(req.getArticleId(), com.blog.entity.Article.ArticleStatus.published)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        if (req.getParentId() != null) {
            Comment parent = commentRepository.findById(req.getParentId())
                    .orElseThrow(() -> new EntityNotFoundException("Parent comment not found"));
            if (parent.getParentId() != null) {
                throw new IllegalArgumentException("Only one-level reply is allowed");
            }
        }

        Comment comment = Comment.builder()
                .articleId(req.getArticleId())
                .nickname(req.getNickname())
                .email(req.getEmail())
                .content(req.getContent())
                .parentId(req.getParentId())
                .status(CommentStatus.pending)
                .build();
        return commentRepository.save(comment);
    }

    public List<Comment> listApproved(Long articleId) {
        return commentRepository.findByArticleIdAndStatusOrderByCreatedAtAsc(articleId, CommentStatus.approved);
    }

    public PageResult<Comment> listAdmin(int page, int size, CommentStatus status, Long articleId) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Comment> result;
        if (status == null) {
            result = commentRepository.findAllByOrderByCreatedAtDesc(pageable);
        } else {
            result = commentRepository.findByStatus(status, pageable);
        }
        List<Comment> list = articleId == null
                ? result.getContent()
                : result.getContent().stream().filter(c -> articleId.equals(c.getArticleId())).toList();
        return new PageResult<>(result.getTotalElements(), safePage, safeSize, list);
    }

    public Comment updateStatus(Long id, CommentStatus status) {
        Comment comment = commentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Comment not found"));
        comment.setStatus(status);
        return commentRepository.save(comment);
    }

    public void delete(Long id) {
        commentRepository.deleteById(id);
    }

    public long countPending() {
        return commentRepository.countByStatus(CommentStatus.pending);
    }
}
