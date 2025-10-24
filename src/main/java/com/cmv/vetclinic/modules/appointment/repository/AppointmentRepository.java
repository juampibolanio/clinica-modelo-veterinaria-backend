package com.cmv.vetclinic.modules.appointment.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.cmv.vetclinic.modules.appointment.model.Appointment;

public interface AppointmentRepository extends JpaRepository<Appointment, Long>, JpaSpecificationExecutor<Appointment> {
    boolean existsByVeterinarian_IdAndDateAndTime(Long vetId, LocalDate date, LocalTime time);

    Optional<Appointment> findByVeterinarian_IdAndDateAndTime(Long vetId, LocalDate date, LocalTime time);

    @EntityGraph(attributePaths = {"veterinarian", "owner", "pet"})
    Optional<Appointment> findWithRelationsById(Long id);
}
