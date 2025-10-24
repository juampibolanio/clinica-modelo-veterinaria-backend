package com.cmv.vetclinic.exceptions.UserExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidUserStateException extends BaseApiException {
    public InvalidUserStateException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}