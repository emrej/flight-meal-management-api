package com.emrej.flightmeal.exception;

public class FlightNotFoundException extends Exception {

    public FlightNotFoundException(String message) {
        super(message);
    }

    public FlightNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
