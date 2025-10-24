package com.cmv.vetclinic.exceptions.AppointmentExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class AppointmentConflictException extends BaseApiException {
    public AppointmentConflictException() {
        super("There is already an appointment scheduled for that veterinarian at this date and time", HttpStatus.CONFLICT);
    }

}
