package com.blog.repository;

import com.blog.entity.Comment;
import com.blog.entity.Comment.CommentStatus;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleIdAndStatusOrderByCreatedAtAsc(Long articleId, CommentStatus status);

    Page<Comment> findByStatus(CommentStatus status, Pageable pageable);

    Page<Comment> findAllByOrderByCreatedAtDesc(Pageable pageable);

    long countByStatus(CommentStatus status);
}
