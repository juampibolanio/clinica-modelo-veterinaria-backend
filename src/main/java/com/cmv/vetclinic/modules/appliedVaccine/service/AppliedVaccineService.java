package com.cmv.vetclinic.modules.appliedVaccine.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineRequest;
import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineResponse;

public interface AppliedVaccineService {
    AppliedVaccineResponse create(AppliedVaccineRequest request);
    AppliedVaccineResponse getById(Long id);
    AppliedVaccineResponse update(Long id, AppliedVaccineRequest request);
    AppliedVaccineResponse patch(Long id, Map<String, Object> updates);
    void delete(Long id);
    Page<AppliedVaccineResponse> list(Long petId, Long vetId, Long productId, LocalDate from, LocalDate to, Pageable pageable);
}