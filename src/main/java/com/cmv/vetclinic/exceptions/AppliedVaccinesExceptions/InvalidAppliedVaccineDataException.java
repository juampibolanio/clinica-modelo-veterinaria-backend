package com.cmv.vetclinic.exceptions.AppliedVaccinesExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidAppliedVaccineDataException extends BaseApiException {
    public InvalidAppliedVaccineDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}