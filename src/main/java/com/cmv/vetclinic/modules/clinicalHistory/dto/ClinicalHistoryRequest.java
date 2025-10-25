package com.cmv.vetclinic.modules.clinicalHistory.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClinicalHistoryRequest {

    @NotBlank(message = "Consultation type is required")
    private String consultationType;

    @NotBlank(message = "Consultation reason is required")
    private String consultationReason;

    private String diagnosis;

    private String treatment;

    @NotNull(message = "Date is required")
    private LocalDate date;

    private String observations;

    @NotNull(message = "Veterinarian ID is required")
    private Long veterinarianId;

    @NotNull(message = "Pet ID is required")
    private Long petId;
}
