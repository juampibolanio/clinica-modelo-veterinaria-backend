package com.cmv.vetclinic.modules.auth.dto;

import com.cmv.vetclinic.modules.user.model.Roles;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Roles role;
}
