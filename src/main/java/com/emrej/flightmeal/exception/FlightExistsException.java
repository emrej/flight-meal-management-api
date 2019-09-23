package com.emrej.flightmeal.exception;

public class FlightExistsException extends Exception {

    public FlightExistsException(String message) {
        super(message);
    }

    public FlightExistsException(String message, Throwable cause) {
        super(message, cause);
    }
}
