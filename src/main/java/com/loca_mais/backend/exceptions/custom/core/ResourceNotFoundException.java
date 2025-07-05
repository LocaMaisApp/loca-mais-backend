package com.loca_mais.backend.exceptions.custom.core;

import com.loca_mais.backend.exceptions.core.ApiException;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {
    public ResourceNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND,message);
    }
}
