package com.cmv.vetclinic.modules.appointment.service;

import java.time.LocalDate;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmv.vetclinic.exceptions.AppointmentExceptions.AppointmentConflictException;
import com.cmv.vetclinic.exceptions.AppointmentExceptions.AppointmentNotFoundException;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentRequest;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentResponse;
import com.cmv.vetclinic.modules.appointment.mapper.AppointmentMapper;
import com.cmv.vetclinic.modules.appointment.model.Appointment;
import com.cmv.vetclinic.modules.appointment.repository.AppointmentRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repo;
    private final AppointmentMapper mapper;

    @Override
    public AppointmentResponse create(AppointmentRequest req) {
        if (repo.existsByVeterinarian_IdAndDateAndTime(req.getVeterinarianId(), req.getDate(), req.getTime())) {
            throw new AppointmentConflictException();
        }
        Appointment appt = mapper.toEntity(req);
        return mapper.toResponse(repo.save(appt));
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        Appointment a = repo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        return mapper.toResponse(a);
    }

    @Override
    public AppointmentResponse update(Long id, AppointmentRequest request) {
        Appointment existing = repo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));

        // check conflicts excluding current appointment
        repo.findByVeterinarian_IdAndDateAndTime(request.getVeterinarianId(), request.getDate(), request.getTime())
                .filter(a -> !a.getId().equals(id))
                .ifPresent(a -> {
                    throw new AppointmentConflictException();
                });

        // Update fields
        existing.setDate(request.getDate());
        existing.setTime(request.getTime());
        existing.setReason(request.getReason());
        existing.setNotes(request.getNotes());
        existing.setStatus(mapper.mapStatus(request.getStatus()));
        return mapper.toResponse(repo.save(existing));
    }

    @Override
    public void delete(Long id) {
        Appointment a = repo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));
        repo.delete(a);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AppointmentResponse> list(Integer page, Integer size, String sortBy, String direction,
            Long veterinarianId, Long ownerId, Long petId,
            String status, LocalDate fromDate, LocalDate toDate) {

        int p = page == null ? 0 : Math.max(page, 0);
        int s = size == null ? 10 : Math.max(size, 1);
        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(p, s, sort);

        Specification<Appointment> spec = AppointmentSpecification.filterBy(veterinarianId, ownerId, petId, status,
                fromDate, toDate);
        return repo.findAll(spec, pageable).map(mapper::toResponse);
    }

}
