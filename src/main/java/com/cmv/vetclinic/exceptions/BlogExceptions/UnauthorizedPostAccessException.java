package com.cmv.vetclinic.exceptions.BlogExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class UnauthorizedPostAccessException extends BaseApiException {
    public UnauthorizedPostAccessException() {
        super("You are not authorized to access this post", HttpStatus.FORBIDDEN);
    }
}