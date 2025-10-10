package com.cmv.vetclinic.config.cloudinary;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cmv.vetclinic.modules.blog.dto.CloudinaryUploadResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CloudinaryServiceImpl implements CloudinaryService{
    private final Cloudinary cloudinary;

    @Override
    public CloudinaryUploadResult uploadFile(MultipartFile file) throws IOException {
        Map<?,?> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        
        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");
        return new CloudinaryUploadResult(url, publicId);
    }

    @Override
    public void deleteFile(String publicId) throws IOException {
        if (publicId == null || publicId.isBlank()) return;
        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }

}