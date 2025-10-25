package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentCount {
    private final Long veterinarianId;
    private final String veterinarianName;
    private final Long totalAppointments;
}
