package com.cmv.vetclinic.modules.categoryProduct.service;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.exceptions.CategoryProductExceptions.CategoryNotFoundException;
import com.cmv.vetclinic.exceptions.CategoryProductExceptions.DuplicateCategoryException;
import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductRequest;
import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductResponse;
import com.cmv.vetclinic.modules.categoryProduct.mapper.CategoryProductMapper;
import com.cmv.vetclinic.modules.categoryProduct.model.CategoryProduct;
import com.cmv.vetclinic.modules.categoryProduct.repository.CategoryProductRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryProductImpl implements CategoryProductService {

    private final CategoryProductRepository repository;
    private final CategoryProductMapper mapper;

    @Override
    @Transactional
    public CategoryProductResponse create(CategoryProductRequest request) {
        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new DuplicateCategoryException(request.getName());
        }

        CategoryProduct entity = mapper.toEntity(request);
        CategoryProduct saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public CategoryProductResponse getById(Long id) {
        CategoryProduct entity = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public CategoryProductResponse update(Long id, CategoryProductRequest request) {
        CategoryProduct entity = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        if (request.getName() != null && !request.getName().equalsIgnoreCase(entity.getName())) {
            if (repository.existsByNameIgnoreCase(request.getName())) {
                throw new DuplicateCategoryException(request.getName());
            }
        }

        mapper.updateEntity(entity, request);
        CategoryProduct saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public CategoryProductResponse patch(Long id, Map<String, Object> updates) {
        CategoryProduct entity = repository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException(id));

        updates.forEach((field, value) -> {
            switch (field) {
                case "name" -> {
                    if (value != null && !((String) value).equalsIgnoreCase(entity.getName())) {
                        if (repository.existsByNameIgnoreCase((String) value)) {
                            throw new DuplicateCategoryException((String) value);
                        }
                        entity.setName((String) value);
                    }
                }
                case "description" -> entity.setDescription((String) value);
                default -> throw new DuplicateCategoryException("Invalid field: " + field);
            }
        });

        CategoryProduct saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new CategoryNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public Page<CategoryProductResponse> list(String keyword, Pageable pageable) {
        Page<CategoryProduct> page = repository.findAll(pageable);
        return page.map(mapper::toResponse);
    }
}
