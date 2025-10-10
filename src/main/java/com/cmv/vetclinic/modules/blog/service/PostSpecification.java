package com.cmv.vetclinic.modules.blog.service;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.cmv.vetclinic.modules.blog.model.Post;

public class PostSpecification {

    public static Specification<Post> hasAuthor(Long authorId) {
        return (root, query, cb) -> cb.equal(root.get("author").get("id"), authorId);
    }

    public static Specification<Post> textContains(String keyword) {
        return (root, query, cb) -> {
            String likePattern = "%" + keyword.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("title")), likePattern),
                    cb.like(cb.lower(root.get("subtitle")), likePattern),
                    cb.like(cb.lower(root.get("content")), likePattern));
        };
    }

    public static Specification<Post> dateBetween(LocalDateTime from, LocalDateTime to) {
        return (root, query, cb) -> cb.between(root.get("publicationDate"), from, to);
    }
}
