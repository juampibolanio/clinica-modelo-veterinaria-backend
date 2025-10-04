package com.cmv.vetclinic.exceptions.UserExceptions;

public class InvalidUserStateException extends RuntimeException {
    public InvalidUserStateException(String message) {
        super(message);
    }
}