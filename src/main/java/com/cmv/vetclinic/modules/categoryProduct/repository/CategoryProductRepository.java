package com.cmv.vetclinic.modules.categoryProduct.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cmv.vetclinic.modules.categoryProduct.model.CategoryProduct;

@Repository
public interface CategoryProductRepository extends JpaRepository<CategoryProduct, Long> {
    boolean existsByNameIgnoreCase(String name);
}
