package com.cmv.vetclinic.modules.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cmv.vetclinic.modules.user.dto.UserRequest;
import com.cmv.vetclinic.modules.user.dto.UserResponse;
import com.cmv.vetclinic.modules.user.mapper.UserMapper;
import com.cmv.vetclinic.modules.user.model.Roles;
import com.cmv.vetclinic.modules.user.model.User;
import com.cmv.vetclinic.modules.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponse createUser(UserRequest request) {
        
        // Validations
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Mapping
        User user = userMapper.toEntity(request);
        user.setRole(Roles.USER);

        // Save in DB
        user = userRepository.save(user);

        // Response
        return userMapper.toResponse(user);
    }
    @Override
    public UserResponse getUserById(Long id) {
        
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        return userMapper.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers() {
        
        return userRepository.findAll()
                .stream()
                .map(userMapper::toResponse)
                .collect(Collectors.toList());

    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {
    
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }

        userRepository.deleteById(id);
    }

}