package com.cmv.vetclinic.modules.categoryProduct.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CategoryProductRequest {
    @NotBlank(message = "Name is required")
    private String name;
    private String description;
}
