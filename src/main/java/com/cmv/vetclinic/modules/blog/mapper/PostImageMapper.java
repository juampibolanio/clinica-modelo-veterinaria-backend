package com.cmv.vetclinic.modules.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cmv.vetclinic.modules.blog.dto.PostImageRequest;
import com.cmv.vetclinic.modules.blog.dto.PostImageResponse;
import com.cmv.vetclinic.modules.blog.model.PostImage;

@Mapper(componentModel = "spring")
public interface PostImageMapper {
    
    PostImageMapper INSTANCE = Mappers.getMapper(PostImageMapper.class);

    PostImageResponse toResponse(PostImage postImage);

    @Mapping(target = "post", ignore = true)
    PostImage toEntity(PostImageRequest request);
}