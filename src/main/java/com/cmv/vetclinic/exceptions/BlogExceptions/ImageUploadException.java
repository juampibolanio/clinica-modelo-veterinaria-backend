package com.cmv.vetclinic.exceptions.BlogExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class ImageUploadException extends BaseApiException {
    public ImageUploadException(String message) {
        super("An error occurred while uploading the image: " + message, HttpStatus.BAD_REQUEST);
    }
}