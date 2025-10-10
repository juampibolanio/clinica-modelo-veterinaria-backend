package com.cmv.vetclinic.modules.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CloudinaryUploadResult {
    private String url;
    private String publicId;
}
