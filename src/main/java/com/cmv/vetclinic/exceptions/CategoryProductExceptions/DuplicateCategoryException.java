package com.cmv.vetclinic.exceptions.CategoryProductExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class DuplicateCategoryException extends BaseApiException {
    public DuplicateCategoryException(String name) {
        super("Category with name '" + name + "' already exists", HttpStatus.BAD_REQUEST);
    }
}