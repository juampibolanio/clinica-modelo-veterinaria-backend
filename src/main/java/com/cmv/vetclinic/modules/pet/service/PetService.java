package com.cmv.vetclinic.modules.pet.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import com.cmv.vetclinic.modules.pet.dto.PetRequest;
import com.cmv.vetclinic.modules.pet.dto.PetResponse;

public interface PetService {
    
    PetResponse createPet(PetRequest request);

    PetResponse getPetById(Long id);

    Page<PetResponse> getAllPets(Integer page,
            Integer size,
            String sortBy,
            String direction,
            String name,
            String species,
            String breed,
            String gender,
            Long ownerId,
            Integer ageMin,
            Integer ageMax,
            Double weightMin,
            Double weightMax);

    List<PetResponse> getPetsByOwnerId(Long ownerId);

    PetResponse partialUpdatePet(Long id, Map<String, Object> updates);

    PetResponse updatePet(Long id, PetRequest request);

    void deletePet(Long id);
}