package com.cmv.vetclinic.modules.clinicalHistory.service;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryRequest;
import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryResponse;

public interface ClinicalHistoryService {
    ClinicalHistoryResponse create(ClinicalHistoryRequest request);

    ClinicalHistoryResponse getById(Long id);

    ClinicalHistoryResponse update(Long id, ClinicalHistoryRequest request);

    ClinicalHistoryResponse patch(Long id, Map<String, Object> updates);

    void delete(Long id);

    Page<ClinicalHistoryResponse> list(Long petId,
            Long veterinarianId,
            String consultationType,
            LocalDate fromDate,
            LocalDate toDate,
            String keyword,
            Pageable pageable);
}
