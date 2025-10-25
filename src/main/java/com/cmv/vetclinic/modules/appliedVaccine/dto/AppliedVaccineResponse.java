package com.cmv.vetclinic.modules.appliedVaccine.dto;

import java.time.LocalDate;

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
public class AppliedVaccineResponse {

    private Long id;
    private LocalDate date;
    private String observations;

    private Long petId;
    private String petName;

    private Long veterinarianId;
    private String veterinarianName;

    private Long productId;
    private String productName;

    private Long clinicalHistoryId;
}