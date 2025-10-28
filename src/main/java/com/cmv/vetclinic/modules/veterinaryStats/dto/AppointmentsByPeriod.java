package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AppointmentsByPeriod {
    private final String period; // Ej: "2025-01" o "Semana 15"
    private final Long totalAppointments;
}