package com.cmv.vetclinic.modules.blog.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.cmv.vetclinic.modules.blog.dto.PostRequest;
import com.cmv.vetclinic.modules.blog.dto.PostResponse;
import com.cmv.vetclinic.modules.blog.model.Post;

@Mapper(componentModel = "spring")
public interface PostMapper {
    
    PostMapper INSTANCE = Mappers.getMapper(PostMapper.class);

    @Mapping(source = "author.name", target = "authorName")
    PostResponse toResponse(Post post);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    @Mapping(target = "publicationDate", ignore = true)
    @Mapping(target = "images", ignore = true)
    Post toEntity(PostRequest request);
}
