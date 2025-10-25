package com.cmv.vetclinic.exceptions.ProductExceptions;
import org.springframework.http.HttpStatus;
import com.cmv.vetclinic.exceptions.BaseApiException;

public class ProductNotFoundException extends BaseApiException {
    public ProductNotFoundException(Long id) {
        super("Product not found with id: " + id, HttpStatus.NOT_FOUND);
    }
}