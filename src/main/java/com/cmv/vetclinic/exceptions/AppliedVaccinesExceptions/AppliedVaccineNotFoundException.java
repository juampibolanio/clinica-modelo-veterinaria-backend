package com.cmv.vetclinic.exceptions.AppliedVaccinesExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class AppliedVaccineNotFoundException extends BaseApiException {
    public AppliedVaccineNotFoundException(Long id) {
        super("Applied vaccine not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
