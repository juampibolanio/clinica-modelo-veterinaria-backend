package com.cmv.vetclinic.modules.clinicalHistory.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.exceptions.ClinicalHistoryExceptions.ClinicalHistoryNotFoundException;
import com.cmv.vetclinic.exceptions.ClinicalHistoryExceptions.InvalidClinicalHistoryDataException;
import com.cmv.vetclinic.modules.appliedVaccine.repository.AppliedVaccineRepository;
import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryRequest;
import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryResponse;
import com.cmv.vetclinic.modules.clinicalHistory.mapper.ClinicalHistoryMapper;
import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;
import com.cmv.vetclinic.modules.clinicalHistory.repository.ClinicalHistoryRepository;
import com.cmv.vetclinic.modules.pet.model.Pet;
import com.cmv.vetclinic.modules.pet.repository.PetRepository;
import com.cmv.vetclinic.modules.user.model.User;
import com.cmv.vetclinic.modules.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClinicalHistoryServiceImpl implements ClinicalHistoryService {

    private final ClinicalHistoryRepository repository;
    private final ClinicalHistoryMapper mapper;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final AppliedVaccineRepository appliedVaccineRepository;

    @Override
    @Transactional
    public ClinicalHistoryResponse create(ClinicalHistoryRequest request) {
        validateRequest(request);

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(
                        () -> new InvalidClinicalHistoryDataException("Pet not found with id: " + request.getPetId()));

        User vet = userRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new InvalidClinicalHistoryDataException(
                        "Veterinarian not found with id: " + request.getVeterinarianId()));

        ClinicalHistory entity = mapper.toEntity(request);
        entity.setPet(pet);
        entity.setVeterinarian(vet);

        ClinicalHistory saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    public ClinicalHistoryResponse getById(Long id) {
        ClinicalHistory ch = repository.findById(id)
                .orElseThrow(() -> new ClinicalHistoryNotFoundException(id));
        return mapper.toResponse(ch);
    }

    @Override
    @Transactional
    public ClinicalHistoryResponse update(Long id, ClinicalHistoryRequest request) {
        validateRequest(request);

        ClinicalHistory existing = repository.findById(id)
                .orElseThrow(() -> new ClinicalHistoryNotFoundException(id));

        if (request.getPetId() != null && !request.getPetId().equals(existing.getPet().getId())) {
            Pet pet = petRepository.findById(request.getPetId())
                    .orElseThrow(() -> new InvalidClinicalHistoryDataException(
                            "Pet not found with id: " + request.getPetId()));
            existing.setPet(pet);
        }

        if (request.getVeterinarianId() != null
                && !request.getVeterinarianId().equals(existing.getVeterinarian().getId())) {
            User vet = userRepository.findById(request.getVeterinarianId())
                    .orElseThrow(() -> new InvalidClinicalHistoryDataException(
                            "Veterinarian not found with id: " + request.getVeterinarianId()));
            existing.setVeterinarian(vet);
        }

        existing.setConsultationType(request.getConsultationType());
        existing.setConsultationReason(request.getConsultationReason());
        existing.setDiagnosis(request.getDiagnosis());
        existing.setTreatment(request.getTreatment());
        existing.setDate(request.getDate());
        existing.setObservations(request.getObservations());

        ClinicalHistory updated = repository.save(existing);
        return mapper.toResponse(updated);
    }

    @Override
    @Transactional
    public ClinicalHistoryResponse patch(Long id, Map<String, Object> updates) {
        ClinicalHistory existing = repository.findById(id)
                .orElseThrow(() -> new ClinicalHistoryNotFoundException(id));

        updates.forEach((field, value) -> {
            switch (field) {
                case "consultationType" -> existing.setConsultationType((String) value);
                case "consultationReason" -> existing.setConsultationReason((String) value);
                case "diagnosis" -> existing.setDiagnosis((String) value);
                case "treatment" -> existing.setTreatment((String) value);
                case "observations" -> existing.setObservations((String) value);

                case "date" -> {
                    if (value != null) {
                        LocalDate parsedDate = value instanceof String
                                ? LocalDate.parse((String) value)
                                : (LocalDate) value;
                        existing.setDate(parsedDate);
                    }
                }

                case "veterinarianId" -> {
                    if (value != null) {
                        Long vetId = ((Number) value).longValue();
                        User vet = userRepository.findById(vetId)
                                .orElseThrow(() -> new InvalidClinicalHistoryDataException(
                                        "Veterinarian not found with id: " + vetId));
                        existing.setVeterinarian(vet);
                    }
                }

                case "petId" -> {
                    if (value != null) {
                        Long petId = ((Number) value).longValue();
                        Pet pet = petRepository.findById(petId)
                                .orElseThrow(() -> new InvalidClinicalHistoryDataException(
                                        "Pet not found with id: " + petId));
                        existing.setPet(pet);
                    }
                }

                case "usedProductIds" -> {
                    System.out.println("Ignoring usedProductIds in patch (not yet implemented)");
                }

                default -> throw new InvalidClinicalHistoryDataException("Invalid field: " + field);
            }
        });

        ClinicalHistory saved = repository.save(existing);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        ClinicalHistory ch = repository.findById(id)
                .orElseThrow(() -> new ClinicalHistoryNotFoundException(id));

        appliedVaccineRepository.deleteAllByClinicalHistoryId(id);

        repository.delete(ch);
    }

    @Override
    public Page<ClinicalHistoryResponse> list(Long petId,
            Long veterinarianId,
            String consultationType,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword,
            Pageable pageable) {
        var spec = ClinicalHistorySpecification.filter(petId, veterinarianId, consultationType, fromDate, toDate,
                keyword);
        Page<ClinicalHistory> page = repository.findAll(spec, pageable);
        return page.map(mapper::toResponse);
    }

    private void validateRequest(ClinicalHistoryRequest request) {
        if (request.getDate() == null) {
            throw new InvalidClinicalHistoryDataException("Date is required");
        }
        if (request.getConsultationType() == null || request.getConsultationType().isBlank()) {
            throw new InvalidClinicalHistoryDataException("Consultation type is required");
        }
    }

}
