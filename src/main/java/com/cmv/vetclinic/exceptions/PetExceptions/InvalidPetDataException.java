package com.cmv.vetclinic.exceptions.PetExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidPetDataException extends BaseApiException {
    public InvalidPetDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}