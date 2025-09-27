package com.cmv.vetclinic.modules.auth.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cmv.vetclinic.modules.auth.dto.AuthRequest;
import com.cmv.vetclinic.modules.auth.dto.AuthResponse;
import com.cmv.vetclinic.modules.auth.dto.RegisterRequest;
import com.cmv.vetclinic.modules.user.dto.UserResponse;
import com.cmv.vetclinic.modules.user.mapper.UserMapper;
import com.cmv.vetclinic.modules.user.model.Roles;
import com.cmv.vetclinic.modules.user.model.User;
import com.cmv.vetclinic.modules.user.repository.UserRepository;
import com.cmv.vetclinic.security.JwtUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService{
    
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public AuthResponse login(AuthRequest request) {
        User user = userRepository.findByUsername(request.getUsername());
        
        if (user == null || !passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        String token = jwtUtil.generateToken(user.getUsername());
        UserResponse userResp = userMapper.toResponse(user);

        return new AuthResponse(
                        token, 
                        userResp.getUsername(), 
                        userResp.getName(), 
                        userResp.getSurname(), 
                        userResp.getEmail(), 
                        userResp.getPhoneNumber(), 
                        userResp.getRole()
                    );

    }
    @Override
    public AuthResponse register(RegisterRequest request) {
        
        User user = new User();

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setSurname(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Roles.USER);

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getUsername());

        UserResponse userResp = userMapper.toResponse(savedUser);

        return new AuthResponse(
                            token, 
                            userResp.getUsername(), 
                            userResp.getName(), 
                            userResp.getSurname(), 
                            userResp.getEmail(), 
                            userResp.getPhoneNumber(), 
                            userResp.getRole()
                        );  
    }
}
