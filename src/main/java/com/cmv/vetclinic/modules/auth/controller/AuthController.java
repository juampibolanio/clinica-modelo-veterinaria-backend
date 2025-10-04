package com.cmv.vetclinic.modules.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cmv.vetclinic.modules.auth.dto.AuthRequest;
import com.cmv.vetclinic.modules.auth.dto.AuthResponse;
import com.cmv.vetclinic.modules.auth.dto.RegisterRequest;
import com.cmv.vetclinic.modules.auth.service.AuthService;
import com.cmv.vetclinic.modules.user.mapper.UserMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserMapper userMapper;

    @PostMapping
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @GetMapping
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}