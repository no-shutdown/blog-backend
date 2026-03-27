package com.blog.repository;

import com.blog.entity.Article;
import com.blog.entity.Article.ArticleStatus;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Page<Article> findByStatus(ArticleStatus status, Pageable pageable);

    @Query("""
            select a from Article a
            where a.status = :status
              and (:categoryId is null or a.category.id = :categoryId)
              and (:tagId is null or exists (select t from a.tags t where t.id = :tagId))
              and (:keyword is null or lower(a.title) like lower(concat('%', :keyword, '%'))
                   or lower(a.summary) like lower(concat('%', :keyword, '%')))
            order by a.createdAt desc
            """)
    Page<Article> searchPublished(
            @Param("status") ArticleStatus status,
            @Param("categoryId") Long categoryId,
            @Param("tagId") Long tagId,
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("""
            select a from Article a
            where (:status is null or a.status = :status)
              and (:keyword is null or lower(a.title) like lower(concat('%', :keyword, '%')))
            order by a.createdAt desc
            """)
    Page<Article> searchAdmin(@Param("status") ArticleStatus status,
                              @Param("keyword") String keyword,
                              Pageable pageable);

    Optional<Article> findByIdAndStatus(Long id, ArticleStatus status);

    long countByStatus(ArticleStatus status);

    @Query("select coalesce(sum(a.viewCount), 0) from Article a")
    Long sumViewCount();
}
