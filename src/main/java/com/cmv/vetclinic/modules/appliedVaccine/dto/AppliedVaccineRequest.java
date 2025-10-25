package com.cmv.vetclinic.modules.appliedVaccine.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedVaccineRequest {

    @NotNull(message = "Pet ID is required")
    private Long petId;

    @NotNull(message = "Veterinarian ID is required")
    private Long veterinarianId;

    @NotNull(message = "Product ID is required")
    private Long productId;

    @NotNull(message = "Clinical history ID is required")
    private Long clinicalHistoryId;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String observations;
}