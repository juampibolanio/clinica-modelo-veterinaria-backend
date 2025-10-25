package com.cmv.vetclinic.modules.appliedVaccine.model;

import java.time.LocalDate;

import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;
import com.cmv.vetclinic.modules.pet.model.Pet;
import com.cmv.vetclinic.modules.product.model.Product;
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
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "applied_vaccines")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppliedVaccine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "applied_vaccine_seq")
    @SequenceGenerator(name = "applied_vaccine_seq", sequenceName = "applied_vaccine_seq", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    @NotNull(message = "Pet is required")
    private Pet pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinarian_id", nullable = false)
    @NotNull(message = "Veterinarian is required")
    private User veterinarian;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    @NotNull(message = "Product is required")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clinical_history_id", nullable = false)
    @NotNull(message = "Clinical history is required")
    private ClinicalHistory clinicalHistory;

    @NotNull(message = "Application date is required")
    @Column(nullable = false)
    private LocalDate date;

    @Column(length = 500)
    private String observations;
}