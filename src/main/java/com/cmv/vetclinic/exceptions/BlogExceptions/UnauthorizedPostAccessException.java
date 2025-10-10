package com.cmv.vetclinic.exceptions.BlogExceptions;

public class UnauthorizedPostAccessException extends RuntimeException {
    public UnauthorizedPostAccessException() {
        super("No tiene permiso para modificar o eliminar este post");
    }
}