package com.cmv.vetclinic.exceptions.PetExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class PetsForOwnerNotFoundException extends BaseApiException {
    public PetsForOwnerNotFoundException(Long ownerId) {
        super("No pets found for owner with ID " + ownerId, HttpStatus.NOT_FOUND);
    }
}
