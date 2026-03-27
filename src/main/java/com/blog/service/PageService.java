package com.blog.service;

import com.blog.entity.Page;
import com.blog.repository.PageRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PageService {
    private final PageRepository pageRepository;

    public Page getBySlug(String slug) {
        return pageRepository.findBySlug(slug)
                .orElseThrow(() -> new EntityNotFoundException("Page not found"));
    }

    public Page update(String slug, Page req) {
        Page page = getBySlug(slug);
        page.setTitle(req.getTitle());
        page.setContent(req.getContent());
        page.setContentType(req.getContentType());
        return pageRepository.save(page);
    }
}
