package com.cmv.vetclinic.exceptions.OwnerExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class OwnerNotFoundException extends BaseApiException {
    public OwnerNotFoundException(Long id) {
        super("Owner with ID " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
