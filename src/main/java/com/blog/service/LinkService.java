package com.blog.service;

import com.blog.entity.Link;
import com.blog.repository.LinkRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LinkService {
    private final LinkRepository linkRepository;

    public List<Link> listVisible() {
        return linkRepository.findByStatusOrderBySortOrderAsc(Link.LinkStatus.visible);
    }

    public List<Link> listAll() {
        return linkRepository.findAll();
    }

    public Link create(Link link) {
        link.setId(null);
        if (link.getStatus() == null) {
            link.setStatus(Link.LinkStatus.visible);
        }
        if (link.getSortOrder() == null) {
            link.setSortOrder(0);
        }
        return linkRepository.save(link);
    }

    public Link update(Long id, Link req) {
        Link link = linkRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Link not found"));
        link.setName(req.getName());
        link.setUrl(req.getUrl());
        link.setAvatar(req.getAvatar());
        link.setDescription(req.getDescription());
        link.setStatus(req.getStatus());
        link.setSortOrder(req.getSortOrder());
        return linkRepository.save(link);
    }

    public void delete(Long id) {
        linkRepository.deleteById(id);
    }
}
