package com.cmv.vetclinic.modules.pet.model;

import java.time.LocalDate;
import java.time.Period;

import com.cmv.vetclinic.modules.owner.model.Owner;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "pets")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "pet_seq")
    @SequenceGenerator(
        name = "pet_seq",
        sequenceName = "pet_seq",
        allocationSize = 1
    )
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 255)
    private String name;

    @NotBlank(message = "Species is required")
    @Column(nullable = false, length = 255)
    private String species;

    @Column(length = 255)
    private String breed;

    @NotNull(message = "Birth date is required")
    @Column(nullable = false)
    private LocalDate birthDate;

    private Integer age;

    @NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    @Builder.Default
    private Genders gender = Genders.MALE;

    @Column(length = 255)
    private String color;

    @NotNull(message = "Weight is required")
    @PositiveOrZero(message = "Weight must be greater than or equal to zero")
    @Column(nullable = false)
    @Builder.Default
    private Double weight = 0.0;

    @Column(length = 255)
    private String allergies;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @PrePersist
    @PreUpdate
    public void updateAge() {
        if (birthDate != null) {
            this.age = Period.between(birthDate, LocalDate.now()).getYears();
        }
    }
}
