package com.aishu.spring_security.Exceptions;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.thymeleaf.exceptions.TemplateInputException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Custom error response model
    private Map<String, Object> buildError(HttpStatus status, String message, HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", status.value());
        error.put("error", status.getReasonPhrase());
        error.put("message", message);
        error.put("path", request.getRequestURI());
        return error;
    }

//    // Handle custom ResourceNotFoundException
//    @ExceptionHandler(ResourceNotFoundException.class)
//    public ResponseEntity<Map<String, Object>> handleNotFound(ResourceNotFoundException ex,
//                                                              HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(buildError(HttpStatus.NOT_FOUND, ex.getMessage(), request));
//    }

    // Handle validation errors (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .findFirst()
                .orElse("Validation failed");
        return ResponseEntity.badRequest()
                .body(buildError(HttpStatus.BAD_REQUEST, message, request));
    }

    // Handle all other exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGeneral(Exception ex,
                                                             HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage(), request));
    }


        @ExceptionHandler(TemplateInputException.class)
        public ResponseEntity<Map<String, Object>> handleTemplateError(TemplateInputException ex,
            HttpServletRequest request) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now());
        error.put("status", HttpStatus.NOT_FOUND.value());
        error.put("error", "Template Not Found");
        error.put("message", "Requested template could not be resolved: " + ex.getTemplateName());
        error.put("path", request.getRequestURI());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);


    }
    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(ExpiredJwtException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Token expired. Please login again.");
    }

    // You can add other handlers for signature errors, malformed tokens, etc.
    @ExceptionHandler(io.jsonwebtoken.SignatureException.class)
    public ResponseEntity<String> handleSignatureException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body("Invalid token signature.");
    }


    }
