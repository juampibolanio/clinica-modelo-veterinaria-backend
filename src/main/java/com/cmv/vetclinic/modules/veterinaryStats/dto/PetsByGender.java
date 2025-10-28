package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PetsByGender {
    private final String gender;
    private final Long count;
}