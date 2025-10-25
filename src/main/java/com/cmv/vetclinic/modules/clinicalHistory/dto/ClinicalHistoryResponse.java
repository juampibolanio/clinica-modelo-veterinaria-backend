package com.cmv.vetclinic.modules.clinicalHistory.dto;

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
public class ClinicalHistoryResponse {
    
    private Long id;
    private String consultationType;
    private String consultationReason;
    private String diagnosis;
    private String treatment;
    private String date;
    private String observations;

    private Long veterinarianId;
    private String veterinarianName;

    private Long petId;
    private String petName;
}
