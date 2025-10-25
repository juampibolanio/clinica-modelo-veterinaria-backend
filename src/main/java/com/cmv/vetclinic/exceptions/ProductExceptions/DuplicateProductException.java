package com.cmv.vetclinic.exceptions.ProductExceptions;

import org.springframework.http.HttpStatus;
import com.cmv.vetclinic.exceptions.BaseApiException;

public class DuplicateProductException extends BaseApiException {
    public DuplicateProductException(String name) {
        super("Product with name '" + name + "' already exists", HttpStatus.BAD_REQUEST);
    }
}
