package com.cmv.vetclinic.config.cloudinary;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.cmv.vetclinic.modules.blog.dto.CloudinaryUploadResult;

public interface CloudinaryService {
    CloudinaryUploadResult uploadFile(MultipartFile file) throws IOException;

    void deleteFile(String publicId) throws IOException;
    
} 