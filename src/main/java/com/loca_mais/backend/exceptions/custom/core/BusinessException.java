package com.loca_mais.backend.exceptions.custom.core;

import com.loca_mais.backend.exceptions.core.ApiException;
import org.springframework.http.HttpStatus;

public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(HttpStatus.BAD_REQUEST, message);
    }
}
