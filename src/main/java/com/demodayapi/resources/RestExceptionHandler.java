package com.demodayapi.resources;


import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.demodayapi.exceptions.UserCPFAlreadyExistsException;
import com.demodayapi.exceptions.UserEmailAlreadyExistsException;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class RestExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<StandardError> handleValidationExceptions(MethodArgumentNotValidException exception, HttpServletRequest request) {
        StandardError err = new StandardError();
        Map<String, String> errors = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.BAD_REQUEST.value());
        err.setErrors(errors);
        err.setMessage("Argumentos Inválidos.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserEmailAlreadyExistsException.class)
    public ResponseEntity<StandardError> handleUserEmaillExistsExceptions(UserEmailAlreadyExistsException exception, HttpServletRequest request) {
        StandardError err = new StandardError();
        Map<String, String> errors = new HashMap<>();
        errors.put("email", exception.getMessage());
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setErrors(errors);
        err.setMessage("Email já cadastrado.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(UserCPFAlreadyExistsException.class)
    public ResponseEntity<StandardError> handleUserCPFExistsExceptions(UserCPFAlreadyExistsException exception, HttpServletRequest request) {
        StandardError err = new StandardError();
        Map<String, String> errors = new HashMap<>();
        errors.put("cpf", exception.getMessage());
        err.setTimestamp(Instant.now());
        err.setStatus(HttpStatus.CONFLICT.value());
        err.setErrors(errors);
        err.setMessage("CPF já cadastrado.");
        err.setPath(request.getRequestURI());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
    }

}