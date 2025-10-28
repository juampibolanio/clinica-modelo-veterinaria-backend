package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DiagnosisBySpecies {
    private final String species;
    private final String diagnosis;
    private final Long count;
}
