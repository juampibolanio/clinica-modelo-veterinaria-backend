package com.cmv.vetclinic.exceptions.ProductExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidProductDataException extends BaseApiException {
    public InvalidProductDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}

