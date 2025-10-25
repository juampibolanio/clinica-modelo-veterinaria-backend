package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PetsPerOwner {
    private final Long ownerId;
    private final String ownerFullName;
    private final Long totalPets;
}
