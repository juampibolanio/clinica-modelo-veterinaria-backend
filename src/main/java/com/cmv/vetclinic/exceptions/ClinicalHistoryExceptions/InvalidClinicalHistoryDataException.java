package com.cmv.vetclinic.exceptions.ClinicalHistoryExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidClinicalHistoryDataException extends BaseApiException{
    public InvalidClinicalHistoryDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
