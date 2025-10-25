package com.cmv.vetclinic.exceptions.CategoryProductExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class CategoryNotFoundException extends BaseApiException{
    public CategoryNotFoundException(Long id) {
        super("Category not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
