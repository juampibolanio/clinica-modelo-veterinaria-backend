package com.cmv.vetclinic.modules.appointment.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmv.vetclinic.exceptions.AppointmentExceptions.AppointmentConflictException;
import com.cmv.vetclinic.exceptions.AppointmentExceptions.AppointmentNotFoundException;
import com.cmv.vetclinic.exceptions.AppointmentExceptions.InvalidAppointmentDataException;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentRequest;
import com.cmv.vetclinic.modules.appointment.dto.AppointmentResponse;
import com.cmv.vetclinic.modules.appointment.mapper.AppointmentMapper;
import com.cmv.vetclinic.modules.appointment.model.Appointment;
import com.cmv.vetclinic.modules.appointment.model.AppointmentStatus;
import com.cmv.vetclinic.modules.appointment.repository.AppointmentRepository;
import com.cmv.vetclinic.modules.owner.repository.OwnerRepository;
import com.cmv.vetclinic.modules.pet.repository.PetRepository;
import com.cmv.vetclinic.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentRepository repo;
    private final AppointmentMapper mapper;
    private final UserRepository userRepo;
    private final OwnerRepository ownerRepo;
    private final PetRepository petRepo;

    @Override
    public AppointmentResponse create(AppointmentRequest req) {
        if (repo.existsByVeterinarian_IdAndDateAndTime(req.getVeterinarianId(), req.getDate(), req.getTime())) {
            throw new AppointmentConflictException();
        }
        Appointment appt = mapper.toEntity(req);
        Appointment saved = repo.save(appt);

        // recover full entity with all relationships
        Appointment full = repo.findWithRelationsById(saved.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(saved.getId()));

        return mapper.toResponse(full);
    }

    @Override
    @Transactional(readOnly = true)
    public AppointmentResponse getById(Long id) {
        Appointment a = repo.findWithRelationsById(id)
                .orElseThrow(() -> new AppointmentNotFoundException(id));
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
    @Transactional
    public AppointmentResponse patch(Long id, Map<String, Object> updates) {
        Appointment a = repo.findById(id).orElseThrow(() -> new AppointmentNotFoundException(id));

        // Copies of current values (in case they change)
        LocalDate newDate = a.getDate();
        LocalTime newTime = a.getTime();
        Long newVetId = a.getVeterinarian() != null ? a.getVeterinarian().getId() : null;
        Long newOwnerId = a.getOwner() != null ? a.getOwner().getId() : null;
        Long newPetId = a.getPet() != null ? a.getPet().getId() : null;

        for (Map.Entry<String, Object> e : updates.entrySet()) {
            String key = e.getKey();
            Object val = e.getValue();
            if (val == null)
                continue; // ignore explicit nulls

            switch (key) {
                case "date" -> {
                    try {
                        newDate = LocalDate.parse(val.toString());
                    } catch (Exception ex) {
                        throw new InvalidAppointmentDataException(
                                "Invalid date format. Expected ISO date (yyyy-MM-dd).");
                    }
                }
                case "time" -> {
                    try {
                        newTime = LocalTime.parse(val.toString());
                    } catch (Exception ex) {
                        throw new InvalidAppointmentDataException("Invalid time format. Expected ISO time (HH:mm:ss).");
                    }
                }
                case "status" -> {
                    try {
                        a.setStatus(AppointmentStatus.valueOf(val.toString().toUpperCase()));
                    } catch (IllegalArgumentException ex) {
                        throw new InvalidAppointmentDataException(
                                "Invalid status. Use PENDING, CONFIRMED or CANCELLED.");
                    }
                }
                case "reason" -> a.setReason(val.toString());
                case "notes" -> a.setNotes(val.toString());

                case "veterinarianId" -> {
                    try {
                        newVetId = Long.valueOf(val.toString());
                    } catch (NumberFormatException ex) {
                        throw new InvalidAppointmentDataException("Invalid veterinarianId.");
                    }
                }
                case "ownerId" -> {
                    try {
                        newOwnerId = Long.valueOf(val.toString());
                    } catch (NumberFormatException ex) {
                        throw new InvalidAppointmentDataException("Invalid ownerId.");
                    }
                }
                case "petId" -> {
                    try {
                        newPetId = Long.valueOf(val.toString());
                    } catch (NumberFormatException ex) {
                        throw new InvalidAppointmentDataException("Invalid petId.");
                    }
                }

                // Unknown fields: silently ignore
                default -> {
                    /* ignore */ }
            }
        }

        // Validate relationships if changed
        if (newVetId == null)
            throw new InvalidAppointmentDataException("Veterinarian ID cannot be null.");
        if (newOwnerId == null)
            throw new InvalidAppointmentDataException("Owner ID cannot be null.");
        if (newPetId == null)
            throw new InvalidAppointmentDataException("Pet ID cannot be null.");
        if (newDate == null)
            throw new InvalidAppointmentDataException("Date cannot be null.");
        if (newTime == null)
            throw new InvalidAppointmentDataException("Time cannot be null.");

        // (Optional) verify and set new relationships if changed 
        if (!newVetId.equals(a.getVeterinarian().getId())) {
            userRepo.findById(newVetId)
                    .orElseThrow(() -> new InvalidAppointmentDataException("Veterinarian not found."));
            a.getVeterinarian().setId(newVetId);
        }
        if (!newOwnerId.equals(a.getOwner().getId())) {
            ownerRepo.findById(newOwnerId).orElseThrow(() -> new InvalidAppointmentDataException("Owner not found."));
            a.getOwner().setId(newOwnerId);
        }
        if (!newPetId.equals(a.getPet().getId())) {
            petRepo.findById(newPetId).orElseThrow(() -> new InvalidAppointmentDataException("Pet not found."));
            a.getPet().setId(newPetId);
        }

        // Check for conflict ONLY if vet/date/time change
        boolean changedSlot = !newDate.equals(a.getDate())
                || !newTime.equals(a.getTime())
                || !newVetId.equals(a.getVeterinarian().getId());

        if (changedSlot) {
            repo.findByVeterinarian_IdAndDateAndTime(newVetId, newDate, newTime)
                    .filter(other -> !other.getId().equals(a.getId()))
                    .ifPresent(other -> {
                        throw new AppointmentConflictException();
                    });
        }

        // finally apply date/time changes
        a.setDate(newDate);
        a.setTime(newTime);

        Appointment saved = repo.save(a);

        // Reload to return full names
        Appointment full = repo.findById(saved.getId())
                .orElseThrow(() -> new AppointmentNotFoundException(saved.getId()));
        return mapper.toResponse(full);
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
