package com.cmv.vetclinic.exceptions.PetExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class PetNotFoundException extends BaseApiException {
    public PetNotFoundException(Long id) {
        super("Pet with ID " + id + " not found", HttpStatus.NOT_FOUND);
    }
}