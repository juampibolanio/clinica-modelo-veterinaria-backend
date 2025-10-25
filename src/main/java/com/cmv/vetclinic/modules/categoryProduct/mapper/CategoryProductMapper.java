package com.cmv.vetclinic.modules.categoryProduct.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductRequest;
import com.cmv.vetclinic.modules.categoryProduct.dto.CategoryProductResponse;
import com.cmv.vetclinic.modules.categoryProduct.model.CategoryProduct;

@Mapper(componentModel = "spring")
public interface CategoryProductMapper {
    
    CategoryProduct toEntity(CategoryProductRequest request);
    CategoryProductResponse toResponse(CategoryProduct entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget CategoryProduct entity, CategoryProductRequest request);
}