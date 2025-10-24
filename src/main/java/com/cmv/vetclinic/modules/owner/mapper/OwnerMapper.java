package com.cmv.vetclinic.modules.owner.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.cmv.vetclinic.modules.owner.dto.OwnerResponse;
import com.cmv.vetclinic.modules.owner.model.Owner;
import com.cmv.vetclinic.modules.pet.mapper.PetMapper;

@Mapper(componentModel = "spring", uses = {PetMapper.class})
public interface OwnerMapper {

    @Mapping(source = "pets", target = "pets")
    OwnerResponse toResponse(Owner owner);
}
