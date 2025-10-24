package com.cmv.vetclinic.modules.owner.model;

import java.util.ArrayList;
import java.util.List;

import com.cmv.vetclinic.modules.pet.model.Pet;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "owners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Owner {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "owner_seq")
    @SequenceGenerator(
        name = "owner_seq",
        sequenceName = "owner_seq",
        allocationSize = 1
    )
    private Long id;
    
    @Column(nullable = true, unique = true, length = 20)
    private String documentNumber;

    @NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Surname is required")
    @Column(nullable = false, length = 100)
    private String surname;

    @Column(length = 20, unique = true)
    private String phoneNumber;

    @Email(message = "Invalid email format")
    @Column(unique = true, length = 254)
    private String email;

    @Column(length = 254)
    private String address;

    @Column(nullable = false)
    @Builder.Default
    private Double totalDebt = 0.0;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Pet> pets = new ArrayList<>();

    /* Auxiliary methods */
    public String getFullName() {
        return name + " " + surname;
    }
}