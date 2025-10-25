package com.cmv.vetclinic.modules.product.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmv.vetclinic.modules.product.dto.ProductRequest;
import com.cmv.vetclinic.modules.product.dto.ProductResponse;

public interface ProductService {
    ProductResponse create(ProductRequest request);
    ProductResponse getById(Long id);
    ProductResponse update(Long id, ProductRequest request);
    ProductResponse patch(Long id, Map<String, Object> updates);
    void delete(Long id);
    Page<ProductResponse> list(String keyword, Pageable pageable);
}