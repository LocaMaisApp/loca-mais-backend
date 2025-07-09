package com.loca_mais.backend.exceptions.handler;

import com.loca_mais.backend.exceptions.core.ApiException;
import io.jsonwebtoken.JwtException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLException;


@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LogManager.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleErrorResponse(ApiException ex) {
        return ApiException.toResponseEntity(ex);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(DuplicateKeyException ex) {
        ApiException apiException = new ApiException(HttpStatus.CONFLICT, ex.getMessage());
        return ApiException.toResponseEntity(apiException);
    }
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<Object> handleSQLException(SQLException ex) {
        ApiException apiException = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        return ApiException.toResponseEntity(apiException);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Object> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        ApiException apiException=new ApiException(HttpStatus.BAD_REQUEST,ex.getMessage());
        return ApiException.toResponseEntity(apiException);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Object> handleDuplicateKeyException(BadCredentialsException ex) {
        ApiException apiException = new ApiException(HttpStatus.BAD_REQUEST, "Credênciais Inválidas");
        return ApiException.toResponseEntity(apiException);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> handleJwtException(JwtException ex) {
        ApiException apiException = new ApiException(HttpStatus.UNAUTHORIZED, "Usuário não autenticado");
        return ApiException.toResponseEntity(apiException);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex) {
        log.error("e: ", ex);
        ApiException apiException = new ApiException(HttpStatus.INTERNAL_SERVER_ERROR, "Erro inesperado");
        return ApiException.toResponseEntity(apiException);
    }


}
