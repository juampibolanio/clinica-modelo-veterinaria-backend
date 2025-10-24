package com.cmv.vetclinic.exceptions;

import org.springframework.http.HttpStatus;

public abstract class BaseApiException extends RuntimeException {

    private final HttpStatus status;

    protected BaseApiException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public HttpStatus getStatus() {
        return status;
    }
}
