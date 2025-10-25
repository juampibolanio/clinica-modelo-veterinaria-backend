package com.cmv.vetclinic.modules.appliedVaccine.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.exceptions.AppliedVaccinesExceptions.AppliedVaccineNotFoundException;
import com.cmv.vetclinic.exceptions.AppliedVaccinesExceptions.InvalidAppliedVaccineDataException;
import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineRequest;
import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineResponse;
import com.cmv.vetclinic.modules.appliedVaccine.mapper.AppliedVaccineMapper;
import com.cmv.vetclinic.modules.appliedVaccine.model.AppliedVaccine;
import com.cmv.vetclinic.modules.appliedVaccine.repository.AppliedVaccineRepository;
import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;
import com.cmv.vetclinic.modules.clinicalHistory.repository.ClinicalHistoryRepository;
import com.cmv.vetclinic.modules.pet.model.Pet;
import com.cmv.vetclinic.modules.pet.repository.PetRepository;
import com.cmv.vetclinic.modules.product.model.Product;
import com.cmv.vetclinic.modules.product.repository.ProductRepository;
import com.cmv.vetclinic.modules.user.model.User;
import com.cmv.vetclinic.modules.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AppliedVaccineServiceImpl implements AppliedVaccineService {

    private final AppliedVaccineRepository repository;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ClinicalHistoryRepository clinicalHistoryRepository;
    private final AppliedVaccineMapper mapper;

    @Override
    @Transactional
    public AppliedVaccineResponse create(AppliedVaccineRequest request) {
        validateRequest(request);

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Pet not found with id: " + request.getPetId()));

        User vet = userRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Veterinarian not found with id: " + request.getVeterinarianId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Product not found with id: " + request.getProductId()));

        ClinicalHistory history = clinicalHistoryRepository.findById(request.getClinicalHistoryId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Clinical history not found with id: " + request.getClinicalHistoryId()));

        AppliedVaccine entity = mapper.toEntity(request);
        entity.setPet(pet);
        entity.setVeterinarian(vet);
        entity.setProduct(product);
        entity.setClinicalHistory(history);

        AppliedVaccine saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    
    @Override
    public AppliedVaccineResponse getById(Long id) {
        AppliedVaccine entity = repository.findById(id)
                .orElseThrow(() -> new AppliedVaccineNotFoundException(id));
        return mapper.toResponse(entity);
    }

    @Override
    @Transactional
    public AppliedVaccineResponse update(Long id, AppliedVaccineRequest request) {
        validateRequest(request);

        AppliedVaccine entity = repository.findById(id)
                .orElseThrow(() -> new AppliedVaccineNotFoundException(id));

        Pet pet = petRepository.findById(request.getPetId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Pet not found with id: " + request.getPetId()));

        User vet = userRepository.findById(request.getVeterinarianId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Veterinarian not found with id: " + request.getVeterinarianId()));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Product not found with id: " + request.getProductId()));

        ClinicalHistory history = clinicalHistoryRepository.findById(request.getClinicalHistoryId())
                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Clinical history not found with id: " + request.getClinicalHistoryId()));

        mapper.updateEntity(entity, request);
        entity.setPet(pet);
        entity.setVeterinarian(vet);
        entity.setProduct(product);
        entity.setClinicalHistory(history);

        AppliedVaccine saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public AppliedVaccineResponse patch(Long id, Map<String, Object> updates) {
        AppliedVaccine entity = repository.findById(id)
                .orElseThrow(() -> new AppliedVaccineNotFoundException(id));

        updates.forEach((field, value) -> {
            switch (field) {
                case "date" -> {
                    if (value != null) {
                        LocalDate parsedDate = value instanceof String
                                ? LocalDate.parse((String) value)
                                : (LocalDate) value;
                        entity.setDate(parsedDate);
                    }
                }
                case "observations" -> entity.setObservations((String) value);
                case "petId" -> {
                    if (value != null) {
                        Pet pet = petRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Pet not found with id: " + value));
                        entity.setPet(pet);
                    }
                }
                case "veterinarianId" -> {
                    if (value != null) {
                        User vet = userRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Veterinarian not found with id: " + value));
                        entity.setVeterinarian(vet);
                    }
                }
                case "productId" -> {
                    if (value != null) {
                        Product product = productRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Product not found with id: " + value));
                        entity.setProduct(product);
                    }
                }
                case "clinicalHistoryId" -> {
                    if (value != null) {
                        ClinicalHistory history = clinicalHistoryRepository.findById(Long.valueOf(value.toString()))
                                .orElseThrow(() -> new InvalidAppliedVaccineDataException("Clinical history not found with id: " + value));
                        entity.setClinicalHistory(history);
                    }
                }
                default -> throw new InvalidAppliedVaccineDataException("Invalid field: " + field);
            }
        });

        AppliedVaccine saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new AppliedVaccineNotFoundException(id);
        }
        repository.deleteById(id);
    }

    @Override
    public Page<AppliedVaccineResponse> list(Long petId, Long vetId, Long productId, LocalDate from, LocalDate to, Pageable pageable) {
        var spec = AppliedVaccineSpecification.filter(petId, vetId, productId, from, to);
        Page<AppliedVaccine> page = repository.findAll(spec, pageable);
        return page.map(mapper::toResponse);
    }

    private void validateRequest(AppliedVaccineRequest request) {
        if (request.getDate() == null) {
            throw new InvalidAppliedVaccineDataException("Application date is required");
        }
    }
}