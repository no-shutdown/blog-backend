package com.blog.service;

import com.blog.dto.ArticleRequest;
import com.blog.dto.PageResult;
import com.blog.entity.Article;
import com.blog.entity.Article.ArticleStatus;
import com.blog.entity.Category;
import com.blog.entity.Tag;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import com.blog.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final CategoryRepository categoryRepository;
    private final TagRepository tagRepository;

    public PageResult<Map<String, Object>> listPublished(int page, int size, Long categoryId, Long tagId, String keyword) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> result = articleRepository.searchPublished(ArticleStatus.published, categoryId, tagId, normalize(keyword), pageable);
        List<Map<String, Object>> list = result.getContent().stream().map(this::toListItem).toList();
        return new PageResult<>(result.getTotalElements(), safePage, safeSize, list);
    }

    public List<Map<String, Object>> archive() {
        List<Article> published = articleRepository.findByStatus(
                ArticleStatus.published,
                Pageable.unpaged(Sort.by(Sort.Direction.DESC, "createdAt"))
        ).getContent();

        Map<String, List<Article>> grouped = new LinkedHashMap<>();
        for (Article article : published) {
            LocalDateTime time = article.getCreatedAt();
            String key = time.getYear() + "-" + time.getMonthValue();
            grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(article);
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, List<Article>> entry : grouped.entrySet()) {
            String[] parts = entry.getKey().split("-");
            int year = Integer.parseInt(parts[0]);
            int month = Integer.parseInt(parts[1]);
            List<Map<String, Object>> articles = entry.getValue().stream()
                    .sorted(Comparator.comparing(Article::getCreatedAt).reversed())
                    .map(a -> {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", a.getId());
                        item.put("title", a.getTitle());
                        item.put("createdAt", a.getCreatedAt());
                        return item;
                    })
                    .toList();

            Map<String, Object> group = new HashMap<>();
            group.put("year", year);
            group.put("month", month);
            group.put("count", articles.size());
            group.put("articles", articles);
            result.add(group);
        }
        return result;
    }

    @Transactional
    public Map<String, Object> getPublishedDetail(Long id) {
        Article article = articleRepository.findByIdAndStatus(id, ArticleStatus.published)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));

        article.setViewCount(article.getViewCount() + 1);
        articleRepository.save(article);
        return toDetail(article);
    }

    public PageResult<Map<String, Object>> listAdmin(int page, int size, ArticleStatus status, String keyword) {
        int safePage = Math.max(page, 1);
        int safeSize = Math.min(Math.max(size, 1), 50);
        Pageable pageable = PageRequest.of(safePage - 1, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<Article> result = articleRepository.searchAdmin(status, normalize(keyword), pageable);
        return new PageResult<>(result.getTotalElements(), safePage, safeSize, result.getContent().stream().map(this::toListItem).toList());
    }

    public Map<String, Object> getById(Long id) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        return toDetail(article);
    }

    @Transactional
    public Map<String, Object> create(ArticleRequest req) {
        Article article = new Article();
        applyRequest(article, req);
        article.setViewCount(0);
        return toDetail(articleRepository.save(article));
    }

    @Transactional
    public Map<String, Object> update(Long id, ArticleRequest req) {
        Article article = articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Article not found"));
        applyRequest(article, req);
        return toDetail(articleRepository.save(article));
    }

    public void delete(Long id) {
        articleRepository.deleteById(id);
    }

    private void applyRequest(Article article, ArticleRequest req) {
        article.setTitle(req.getTitle());
        article.setContent(req.getContent());
        article.setContentType(req.getContentType());
        article.setSummary(req.getSummary());
        article.setCoverImage(req.getCoverImage());
        article.setStatus(req.getStatus());

        if (req.getCategoryId() != null) {
            Category category = categoryRepository.findById(req.getCategoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Category not found"));
            article.setCategory(category);
        } else {
            article.setCategory(null);
        }

        Set<Tag> tags = req.getTagIds() == null ? Set.of() : req.getTagIds().stream()
                .map(tagId -> tagRepository.findById(tagId)
                        .orElseThrow(() -> new EntityNotFoundException("Tag not found: " + tagId)))
                .collect(Collectors.toSet());
        article.setTags(tags);
    }

    private Map<String, Object> toListItem(Article article) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", article.getId());
        item.put("title", article.getTitle());
        item.put("summary", article.getSummary());
        item.put("coverImage", article.getCoverImage());
        item.put("createdAt", article.getCreatedAt());
        item.put("viewCount", article.getViewCount());
        item.put("status", article.getStatus());
        item.put("contentType", article.getContentType());
        item.put("category", toCategoryMap(article.getCategory()));
        item.put("tags", article.getTags().stream().map(this::toTagMap).toList());
        return item;
    }

    private Map<String, Object> toDetail(Article article) {
        Map<String, Object> item = toListItem(article);
        item.put("content", article.getContent());
        item.put("updatedAt", article.getUpdatedAt());
        return item;
    }

    private Map<String, Object> toCategoryMap(Category category) {
        if (category == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<>();
        map.put("id", category.getId());
        map.put("name", category.getName());
        map.put("description", category.getDescription());
        return map;
    }

    private Map<String, Object> toTagMap(Tag tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", tag.getId());
        map.put("name", tag.getName());
        map.put("color", tag.getColor());
        return map;
    }

    private String normalize(String keyword) {
        return (keyword == null || keyword.isBlank()) ? null : keyword.trim();
    }
}
