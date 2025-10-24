package com.cmv.vetclinic.exceptions.AppointmentExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class AppointmentNotFoundException extends BaseApiException {
    public AppointmentNotFoundException(Long id) {
        super("Appointment with ID " + id + " not found", HttpStatus.NOT_FOUND);
    }
}
