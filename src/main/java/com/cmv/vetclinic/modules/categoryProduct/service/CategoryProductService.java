package com.cmv.vetclinic.modules.categoryProduct.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductRequest;
import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductResponse;

public interface CategoryProductService {
    CategoryProductResponse create(CategoryProductRequest request);
    CategoryProductResponse getById(Long id);
    CategoryProductResponse update(Long id, CategoryProductRequest request);
    CategoryProductResponse patch(Long id, Map<String, Object> updates);
    void delete(Long id);
    Page<CategoryProductResponse> list(String keyword, Pageable pageable);
}
