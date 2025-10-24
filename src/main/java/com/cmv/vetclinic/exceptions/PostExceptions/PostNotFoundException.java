package com.cmv.vetclinic.exceptions.PostExceptions;

import org.springframework.http.HttpStatus;

import com.cmv.vetclinic.exceptions.BaseApiException;

public class PostNotFoundException extends BaseApiException {
    public PostNotFoundException(Long id) {
        super("Post not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}
