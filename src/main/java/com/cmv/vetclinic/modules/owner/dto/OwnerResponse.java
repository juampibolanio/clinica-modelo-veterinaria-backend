package com.cmv.vetclinic.modules.owner.dto;

import java.util.List;

import com.cmv.vetclinic.modules.pet.dto.PetResponse;

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
public class OwnerResponse {
    private Long id;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private String address;
    private String documentNumber;
    private Double totalDebt;
    private List<PetResponse> pets;
    
}