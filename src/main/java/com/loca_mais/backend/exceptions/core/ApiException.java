package com.loca_mais.backend.exceptions.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@JsonIgnoreProperties({"cause", "stackTrace", "suppressed", "localizedMessage"})
@Getter

public class ApiException extends RuntimeException {
    private HttpStatus status;
    private Integer statusCode;
    private String message;
    private LocalDateTime timestamp;

    public ApiException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.statusCode = status.value();
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public static ResponseEntity<Object> toResponseEntity(ApiException ex) {
        return new ResponseEntity<>(ex, ex.getStatus());
    }

}
