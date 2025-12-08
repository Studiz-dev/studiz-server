package com.studiz.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {
        log.error("CustomException: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(e.getErrorCode());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException e) {
        log.error("ValidationException: {}", e.getMessage(), e);
        
        StringBuilder message = new StringBuilder();
        for (FieldError error : e.getBindingResult().getFieldErrors()) {
            message.append(error.getField())
                    .append(": ")
                    .append(error.getDefaultMessage())
                    .append("; ");
        }
        
        ErrorResponse response = ErrorResponse.of(HttpStatus.BAD_REQUEST, message.toString());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(response);
    }
    
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUsernameNotFoundException(UsernameNotFoundException e) {
        log.warn("UsernameNotFoundException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(ErrorCode.USER_NOT_FOUND);
        return ResponseEntity
                .status(ErrorCode.USER_NOT_FOUND.getStatus())
                .body(response);
    }
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException e) {
        log.warn("BadCredentialsException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_PASSWORD);
        return ResponseEntity
                .status(ErrorCode.INVALID_PASSWORD.getStatus())
                .body(response);
    }
    
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException e) {
        log.warn("AuthenticationException: {}", e.getMessage());
        ErrorResponse response = ErrorResponse.of(ErrorCode.INVALID_PASSWORD);
        return ResponseEntity
                .status(ErrorCode.INVALID_PASSWORD.getStatus())
                .body(response);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

