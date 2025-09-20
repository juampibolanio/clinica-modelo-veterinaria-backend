package com.cmv.vetclinic.modules.user.dto;

import com.cmv.vetclinic.modules.user.model.Roles;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserResponse {
    private Long id;
    private String username;
    private String name;
    private String surname;
    private String email;
    private String phoneNumber;
    private Roles role;
}
