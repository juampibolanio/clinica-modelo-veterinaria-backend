package com.cmv.vetclinic.modules.appliedVaccine.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.NullValuePropertyMappingStrategy;

import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineRequest;
import com.cmv.vetclinic.modules.appliedVaccine.dto.AppliedVaccineResponse;
import com.cmv.vetclinic.modules.appliedVaccine.model.AppliedVaccine;
import com.cmv.vetclinic.modules.user.model.User;

@Mapper(componentModel = "spring")
public interface AppliedVaccineMapper {

    @Mapping(source = "petId", target = "pet.id")
    @Mapping(source = "veterinarianId", target = "veterinarian.id")
    @Mapping(source = "productId", target = "product.id")
    @Mapping(source = "clinicalHistoryId", target = "clinicalHistory.id")
    AppliedVaccine toEntity(AppliedVaccineRequest request);

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(source = "veterinarian.id", target = "veterinarianId")
    @Mapping(source = "veterinarian", target = "veterinarianName", qualifiedByName = "fullVetName")
    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "clinicalHistory.id", target = "clinicalHistoryId")
    AppliedVaccineResponse toResponse(AppliedVaccine entity);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntity(@MappingTarget AppliedVaccine entity, AppliedVaccineRequest request);

    @Named("fullVetName")
    default String mapFullVetName(User user) {
        if (user == null) return null;
        return user.getName() + " " + user.getSurname();
    }
}