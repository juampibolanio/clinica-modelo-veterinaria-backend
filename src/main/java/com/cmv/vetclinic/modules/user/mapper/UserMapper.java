package com.cmv.vetclinic.modules.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.cmv.vetclinic.modules.user.dto.UserRequest;
import com.cmv.vetclinic.modules.user.dto.UserResponse;
import com.cmv.vetclinic.modules.user.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse toResponse(User user);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "role", ignore = true )
    User toEntity(UserRequest request);
}
