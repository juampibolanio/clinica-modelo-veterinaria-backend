package com.cmv.vetclinic.exceptions.UserExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class UserNotFoundException extends BaseApiException {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
