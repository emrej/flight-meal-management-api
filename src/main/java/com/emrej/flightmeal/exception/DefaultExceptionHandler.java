package com.emrej.flightmeal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.TransactionSystemException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class DefaultExceptionHandler {

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.BAD_REQUEST, reason="Data integrity violation")
    @ExceptionHandler(ConstraintViolationException.class)
    public void constraintViolation() {

    }

    @ExceptionHandler(TransactionSystemException.class)
    public ResponseEntity handleConstraintViolation(Exception ex, WebRequest request) {
        Throwable cause = ((TransactionSystemException) ex).getRootCause();
        if (cause instanceof ConstraintViolationException) {
            return ResponseEntity.badRequest().body("{\"message\": \"Data integrity violation\"}");
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.CONFLICT, reason="Flight already exists on this date with that flight number")
    @ExceptionHandler(FlightExistsException.class)
    public void flightExists() {

    }

    // Convert a predefined exception to an HTTP Status code
    @ResponseStatus(value= HttpStatus.NOT_FOUND, reason="Flight not found on this date with that flight number")
    @ExceptionHandler(FlightNotFoundException.class)
    public void flightNotFound() {

    }
}
