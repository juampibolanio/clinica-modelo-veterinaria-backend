package com.cmv.vetclinic.exceptions.UserExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class EmailAlreadyExistsException extends BaseApiException {
    public EmailAlreadyExistsException(String message){
        super(message, HttpStatus.CONFLICT);
    }
}