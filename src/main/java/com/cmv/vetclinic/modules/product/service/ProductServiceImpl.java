package com.cmv.vetclinic.modules.product.service;

import com.cmv.vetclinic.exceptions.ProductExceptions.DuplicateProductException;
import com.cmv.vetclinic.exceptions.ProductExceptions.InvalidProductDataException;
import com.cmv.vetclinic.exceptions.ProductExceptions.ProductNotFoundException;
import com.cmv.vetclinic.modules.categoryProduct.model.CategoryProduct;
import com.cmv.vetclinic.modules.categoryProduct.repository.CategoryProductRepository;
import com.cmv.vetclinic.modules.product.dto.*;
import com.cmv.vetclinic.modules.product.mapper.ProductMapper;
import com.cmv.vetclinic.modules.product.model.Product;
import com.cmv.vetclinic.modules.product.repository.ProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repository;
    private final CategoryProductRepository categoryRepository;
    private final ProductMapper mapper;

    @Override
    @Transactional
    public ProductResponse create(ProductRequest request) {
        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateProductException(request.getName());
        }

        CategoryProduct category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new InvalidProductDataException("Category not found with id: " + request.getCategoryId()));

        Product entity = mapper.toEntity(request);
        entity.setCategory(category);

        Product saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ProductResponse getById(Long id) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ProductResponse update(Long id, ProductRequest request) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        if (!entity.getName().equalsIgnoreCase(request.getName()) &&
            repository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateProductException(request.getName());
        }

        CategoryProduct category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new InvalidProductDataException("Category not found with id: " + request.getCategoryId()));

        mapper.updateEntity(entity, request);
        entity.setCategory(category);

        Product saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ProductResponse patch(Long id, Map<String, Object> updates) {
        Product entity = repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        updates.forEach((field, value) -> {
            switch (field) {
                case "name" -> {
                    if (value != null && !((String) value).equalsIgnoreCase(entity.getName())) {
                        if (repository.existsByNameIgnoreCase((String) value)) {
                            throw new DuplicateProductException((String) value);
                        }
                        entity.setName((String) value);
                    }
                }
                case "type" -> entity.setType((String) value);
                case "stock" -> entity.setStock((Integer) value);
                case "categoryId" -> {
                    if (value != null) {
                        CategoryProduct category = categoryRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new InvalidProductDataException("Category not found with id: " + value));
                        entity.setCategory(category);
                    }
                }
                default -> throw new InvalidProductDataException("Invalid field: " + field);
            }
        });

        Product saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public Page<ProductResponse> list(String keyword, Pageable pageable) {
        Page<Product> page = repository.findAll(pageable);
        return page.map(mapper::toResponse);
    }
}