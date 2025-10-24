package com.cmv.vetclinic.exceptions.OwnerExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidOwnerDataException extends BaseApiException {
    public InvalidOwnerDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
