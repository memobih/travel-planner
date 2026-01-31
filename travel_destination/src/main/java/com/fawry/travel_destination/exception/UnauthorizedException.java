package com.fawry.travel_destination.exception;

public class UnauthorizedException extends RuntimeException 
 {
    public UnauthorizedException(String message) { 
        super(message);
    }
}
