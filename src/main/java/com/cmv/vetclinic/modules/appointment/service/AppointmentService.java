package com.cmv.vetclinic.modules.appointment.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;

import com.cmv.vetclinic.modules.appointment.dto.AppointmentRequest;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentResponse;

public interface AppointmentService {
    AppointmentResponse create(AppointmentRequest request);

    AppointmentResponse getById(Long id);

    AppointmentResponse update(Long id, AppointmentRequest request);

    AppointmentResponse patch(Long id, java.util.Map<String, Object> updates);

    void delete(Long id);

    Page<AppointmentResponse> list(Integer page, Integer size, String sortBy, String direction,
            Long veterinarianId, Long ownerId, Long petId,
            String status, LocalDate fromDate, LocalDate toDate);
}
