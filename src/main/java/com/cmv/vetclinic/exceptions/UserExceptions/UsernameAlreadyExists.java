package com.cmv.vetclinic.exceptions.UserExceptions;

public class UsernameAlreadyExists extends RuntimeException{
    public UsernameAlreadyExists(String message) {
        super(message);
    }
}