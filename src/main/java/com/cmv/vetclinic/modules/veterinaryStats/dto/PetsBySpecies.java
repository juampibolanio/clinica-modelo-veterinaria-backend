package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PetsBySpecies {
    private final String species;
    private final Long count;
}