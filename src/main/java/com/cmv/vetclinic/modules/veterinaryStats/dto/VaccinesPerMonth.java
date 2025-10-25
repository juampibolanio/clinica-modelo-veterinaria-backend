package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class VaccinesPerMonth {
    private final Integer year;
    private final Integer month; 
    private final Long totalApplied;
}
