package com.cmv.vetclinic.exceptions.AppointmentExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class InvalidAppointmentDataException extends BaseApiException {
    public InvalidAppointmentDataException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}
