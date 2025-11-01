package com.cmv.vetclinic.modules.product.service;

import org.springframework.data.jpa.domain.Specification;

import com.cmv.vetclinic.modules.product.model.Product;

public class ProductSpecification {

    public static Specification<Product> nameContains(String name) {
        return (root, query, cb) -> {
            if (name == null || name.isBlank()) return null;
            return cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Product> typeEquals(String type) {
        return (root, query, cb) -> {
            if (type == null || type.isBlank()) return null;
            return cb.equal(root.get("type"), type);
        };
    }

    public static Specification<Product> categoryEquals(String categoryName) {
        return (root, query, cb) -> {
            if (categoryName == null || categoryName.isBlank()) return null;
            return cb.equal(root.get("category").get("name"), categoryName);
        };
    }
}