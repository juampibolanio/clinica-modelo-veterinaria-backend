package com.cmv.vetclinic.modules.clinicalHistory.model;

import java.time.LocalDate;

import com.cmv.vetclinic.modules.pet.model.Pet;
import com.cmv.vetclinic.modules.user.model.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "clinical_histories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ClinicalHistory {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "clinical_history_seq")
    @SequenceGenerator(
        name = "clinical_history_seq", 
        sequenceName = "clinical_history_seq", 
        allocationSize = 1)
    private
    Long id;

    @NotBlank(message = "Consultation type is required")
    @Column(nullable = false, length = 100)
    private String consultationType;

    @NotBlank(message = "Consultation reason is required")
    @Column(nullable = false, length = 255)
    private String consultationReason;

    @Column(length = 500)
    private String diagnosis;

    @Column(length = 500)
    private String treatment;

    @NotNull(message = "Consultation date is required")
    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String observations;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    private User veterinarian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
}
