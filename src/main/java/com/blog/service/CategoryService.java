package com.blog.service;

import com.blog.entity.Article.ArticleStatus;
import com.blog.entity.Category;
import com.blog.repository.ArticleRepository;
import com.blog.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ArticleRepository articleRepository;

    public List<Map<String, Object>> listPublic() {
        return categoryRepository.findAll().stream().map(this::toMapWithCount).toList();
    }

    public List<Category> listAdmin() {
        return categoryRepository.findAll();
    }

    public Category create(Category category) {
        category.setId(null);
        return categoryRepository.save(category);
    }

    public Category update(Long id, Category req) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category not found"));
        category.setName(req.getName());
        category.setDescription(req.getDescription());
        return categoryRepository.save(category);
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private Map<String, Object> toMapWithCount(Category category) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", category.getId());
        map.put("name", category.getName());
        map.put("description", category.getDescription());
        map.put("articleCount", articleRepository.searchPublished(ArticleStatus.published, category.getId(), null, null,
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements());
        return map;
    }
}
