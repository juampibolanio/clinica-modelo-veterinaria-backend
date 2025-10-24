package com.cmv.vetclinic.modules.appointment.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AppointmentResponse {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private String status;
    private String reason;
    private String notes;

    private Long veterinarianId;
    private String veterinarianName;
    private Long ownerId;
    private String ownerName;
    private Long petId;
    private String petName;
}
