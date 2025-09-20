package com.cmv.vetclinic.modules.user.service;

import java.util.List;

import com.cmv.vetclinic.modules.user.dto.UserRequest;
import com.cmv.vetclinic.modules.user.dto.UserResponse;

public interface UserService {

    UserResponse createUser(UserRequest request);

    UserResponse getUserById(Long id);

    UserResponse getUserByUsername(String username);

    List<UserResponse> getAllUsers();

    UserResponse updateUser(Long id, UserRequest request);

    void deleteUser(Long id);

}
