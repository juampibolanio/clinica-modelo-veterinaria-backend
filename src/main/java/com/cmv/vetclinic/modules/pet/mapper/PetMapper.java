package com.cmv.vetclinic.modules.pet.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.cmv.vetclinic.modules.pet.dto.PetRequest;
import com.cmv.vetclinic.modules.pet.dto.PetResponse;
import com.cmv.vetclinic.modules.pet.model.Pet;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "ownerId", target = "owner.id")
    Pet toEntity(PetRequest request);

    @Mapping(source = "owner.id", target = "ownerId")
    PetResponse toResponse(Pet pet);
}
