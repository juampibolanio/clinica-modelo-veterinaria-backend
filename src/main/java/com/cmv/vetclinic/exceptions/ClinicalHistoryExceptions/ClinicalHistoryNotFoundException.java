package com.cmv.vetclinic.exceptions.ClinicalHistoryExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class ClinicalHistoryNotFoundException extends BaseApiException{
    public ClinicalHistoryNotFoundException(Long id) {
        super("Clinical history not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
