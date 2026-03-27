package com.blog.service;

import com.blog.entity.Article.ArticleStatus;
import com.blog.entity.Tag;
import com.blog.repository.ArticleRepository;
import com.blog.repository.TagRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;
    private final ArticleRepository articleRepository;

    public List<Map<String, Object>> listPublic() {
        return tagRepository.findAll().stream().map(this::toMapWithCount).toList();
    }

    public List<Tag> listAdmin() {
        return tagRepository.findAll();
    }

    public Tag create(Tag tag) {
        tag.setId(null);
        return tagRepository.save(tag);
    }

    public Tag update(Long id, Tag req) {
        Tag tag = tagRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found"));
        tag.setName(req.getName());
        tag.setColor(req.getColor());
        return tagRepository.save(tag);
    }

    public void delete(Long id) {
        tagRepository.deleteById(id);
    }

    private Map<String, Object> toMapWithCount(Tag tag) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", tag.getId());
        map.put("name", tag.getName());
        map.put("color", tag.getColor());
        long count = articleRepository.searchPublished(ArticleStatus.published, null, tag.getId(), null, Pageable.unpaged())
                .getTotalElements();
        map.put("articleCount", count);
        return map;
    }
}
