package com.cmv.vetclinic.modules.appointment.service;

import java.time.LocalDate;

import org.springframework.data.jpa.domain.Specification;

import com.cmv.vetclinic.modules.appointment.model.Appointment;
import com.cmv.vetclinic.modules.appointment.model.AppointmentStatus;

import jakarta.persistence.criteria.Predicate;

public class AppointmentSpecification {
    public static Specification<Appointment> filterBy(
            Long veterinarianId, Long ownerId, Long petId,
            String status, LocalDate fromDate, LocalDate toDate) {
        return (root, query, cb) -> {
            Predicate p = cb.conjunction();

            if (veterinarianId != null)
                p = cb.and(p, cb.equal(root.get("veterinarian").get("id"), veterinarianId));
            if (ownerId != null)
                p = cb.and(p, cb.equal(root.get("owner").get("id"), ownerId));
            if (petId != null)
                p = cb.and(p, cb.equal(root.get("pet").get("id"), petId));

            if (status != null && !status.isBlank()) {
                try {
                    AppointmentStatus st = AppointmentStatus.valueOf(status.toUpperCase());
                    p = cb.and(p, cb.equal(root.get("status"), st));
                } catch (IllegalArgumentException ignored) {
                }
            }

            if (fromDate != null)
                p = cb.and(p, cb.greaterThanOrEqualTo(root.get("date"), fromDate));
            if (toDate != null)
                p = cb.and(p, cb.lessThanOrEqualTo(root.get("date"), toDate));

            return p;
        };
    }
}
