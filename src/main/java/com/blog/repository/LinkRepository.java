package com.blog.repository;

import com.blog.entity.Link;
import com.blog.entity.Link.LinkStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByStatusOrderBySortOrderAsc(LinkStatus status);
}
