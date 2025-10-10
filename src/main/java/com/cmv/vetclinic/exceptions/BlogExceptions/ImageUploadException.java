package com.cmv.vetclinic.exceptions.BlogExceptions;

public class ImageUploadException extends RuntimeException {
    public ImageUploadException(String message) {
        super("Error al subir la imagen: " + message);
    }
}