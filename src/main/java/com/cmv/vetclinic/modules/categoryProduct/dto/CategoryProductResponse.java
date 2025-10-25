package com.cmv.vetclinic.modules.categoryProduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryProductResponse {
    private Long id;
    private String name;
    private String description;
}
