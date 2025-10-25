package com.cmv.vetclinic.modules.veterinaryStats.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TopProduct {
    private final Long productId;
    private final String productName;
    private final Long timesUsed;
}
