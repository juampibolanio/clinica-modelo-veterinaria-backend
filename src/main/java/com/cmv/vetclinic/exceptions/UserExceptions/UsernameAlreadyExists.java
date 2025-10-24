package com.cmv.vetclinic.exceptions.UserExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class UsernameAlreadyExists extends BaseApiException {
    public UsernameAlreadyExists(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}