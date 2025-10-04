package com.cmv.vetclinic.modules.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmv.vetclinic.exceptions.UserExceptions.EmailAlreadyExistsException;
import com.cmv.vetclinic.exceptions.UserExceptions.UserNotFoundException;
import com.cmv.vetclinic.exceptions.UserExceptions.UsernameAlreadyExists;
import com.cmv.vetclinic.modules.user.dto.UserRequest;
import com.cmv.vetclinic.modules.user.dto.UserResponse;
import com.cmv.vetclinic.modules.user.dto.UserUpdateRequest;
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
            throw new EmailAlreadyExistsException("Email " + request.getEmail() + " already exists");
        }
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExists("Username " + request.getUsername() + " already exists");
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
                    .orElseThrow(() -> new UserNotFoundException("User with id N°" + id + " not found."));
        return userMapper.toResponse(user);
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        
        if (user == null) {
            throw new UserNotFoundException("User with username " + username + " not found");
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
                    .orElseThrow(() -> new UserNotFoundException("User with id N°" + id + " not found."));

        user.setUsername(request.getUsername());
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Transactional
    @Override
    public UserResponse updatePartialUser(Long id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                    .orElseThrow(() -> new UserNotFoundException("User with id N°" + id + " not found."));

        if (request.getUsername() != null) user.setUsername(request.getUsername());
        if (request.getName() != null) user.setName(request.getName());
        if (request.getSurname() != null) user.setSurname(request.getSurname());
        if (request.getEmail() != null) user.setEmail(request.getEmail());
        if (request.getPassword() != null) user.setPassword(request.getPassword());
        if (request.getPhoneNumber() != null) user.setPhoneNumber(request.getPhoneNumber());

        userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User with id N°" + id + " not found.");
        }
        userRepository.deleteById(id);
    }

    
}