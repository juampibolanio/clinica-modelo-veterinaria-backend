package com.cmv.vetclinic.exceptions.UserExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidUsernamePasswordException extends BaseApiException{
    public InvalidUsernamePasswordException() {
        super("Invalid username or password", HttpStatus.UNAUTHORIZED);
    }
}
