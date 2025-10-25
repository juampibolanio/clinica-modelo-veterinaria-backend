package com.cmv.vetclinic.modules.clinicalHistory.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryRequest;
import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryResponse;
import com.cmv.vetclinic.modules.clinicalHistory.service.ClinicalHistoryService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/clinicalHistory")
@RequiredArgsConstructor
public class ClinicalHistoryController {

    private final ClinicalHistoryService service;

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping
    public ResponseEntity<Page<ClinicalHistoryResponse>> list(
            @RequestParam(required = false) Long petId,
            @RequestParam(required = false) Long veterinarianId,
            @RequestParam(required = false) String consultationType,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "date") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {
        Sort sort = "desc".equalsIgnoreCase(direction) ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<ClinicalHistoryResponse> result = service.list(petId, veterinarianId, consultationType, fromDate, toDate,
                keyword, pageable);
        return ResponseEntity.ok(result);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @GetMapping("/{id}")
    public ResponseEntity<ClinicalHistoryResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(service.getById(id));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PostMapping
    public ResponseEntity<ClinicalHistoryResponse> create(@Valid @RequestBody ClinicalHistoryRequest request) {
        ClinicalHistoryResponse created = service.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PutMapping("/{id}")
    public ResponseEntity<ClinicalHistoryResponse> update(@PathVariable Long id,
            @Valid @RequestBody ClinicalHistoryRequest request) {
        return ResponseEntity.ok(service.update(id, request));
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @PatchMapping("/{id}")
    public ResponseEntity<ClinicalHistoryResponse> patch(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        ClinicalHistoryResponse updated = service.patch(id, updates);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
