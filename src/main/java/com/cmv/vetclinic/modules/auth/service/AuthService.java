package com.cmv.vetclinic.modules.auth.service;

import com.cmv.vetclinic.modules.auth.dto.AuthRequest;
import com.cmv.vetclinic.modules.auth.dto.AuthResponse;
import com.cmv.vetclinic.modules.auth.dto.RegisterRequest;

public interface AuthService {
    AuthResponse login(AuthRequest authRequest);
    AuthResponse register(RegisterRequest registerRequest);
}
