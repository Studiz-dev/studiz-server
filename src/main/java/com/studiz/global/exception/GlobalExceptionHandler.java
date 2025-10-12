package com.studiz.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        log.error("Exception: {}", e.getMessage(), e);
        ErrorResponse response = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}

