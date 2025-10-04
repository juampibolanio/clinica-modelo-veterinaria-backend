package com.cmv.vetclinic.modules.user.dto;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String username;
    private String name;
    private String surname;
    @Email(message = "Invalid email")
    private String email;
    private String password;
    private String phoneNumber;
}
