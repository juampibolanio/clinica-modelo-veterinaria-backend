package com.cmv.vetclinic.modules.clinicalHistory.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryRequest;
import com.cmv.vetclinic.modules.clinicalHistory.dto.ClinicalHistoryResponse;
import com.cmv.vetclinic.modules.clinicalHistory.model.ClinicalHistory;
import com.cmv.vetclinic.modules.user.model.User;

@Mapper(componentModel = "spring")
public interface ClinicalHistoryMapper {

    @Mapping(source = "veterinarianId", target = "veterinarian.id")
    @Mapping(source = "petId", target = "pet.id")
    ClinicalHistory toEntity(ClinicalHistoryRequest request);

    @Mapping(source = "veterinarian.id", target = "veterinarianId")
    @Mapping(source = "veterinarian", target = "veterinarianName", qualifiedByName = "fullVetName")
    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    ClinicalHistoryResponse toResponse(ClinicalHistory history);

    @Named("fullVetName")
    default String mapFullVetName(User user) {
        if (user == null)
            return null;
        return user.getName() + " " + user.getSurname();
    }
}
