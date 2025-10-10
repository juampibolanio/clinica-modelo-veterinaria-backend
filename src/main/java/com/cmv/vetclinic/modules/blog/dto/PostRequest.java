package com.cmv.vetclinic.modules.blog.dto;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
public class PostRequest {

    @NotBlank
    @Size(max = 150)
    private String title;

    @Size(max = 200)
    private String subtitle;

    @NotBlank
    private String content;
    
    private List<String> imageDescriptions;

}
