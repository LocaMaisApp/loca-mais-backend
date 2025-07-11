package com.loca_mais.backend.exceptions.custom.core;

import com.loca_mais.backend.exceptions.core.ApiException;
import org.springframework.http.HttpStatus;

public class EntityNotFoundException extends ApiException {
    public EntityNotFoundException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }

    public EntityNotFoundException(String message, HttpStatus status) {
        super(status, message);
    }
}
