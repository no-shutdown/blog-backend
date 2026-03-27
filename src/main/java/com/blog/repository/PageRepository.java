package com.blog.repository;

import com.blog.entity.Page;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PageRepository extends JpaRepository<Page, Long> {
    Optional<Page> findBySlug(String slug);
}
