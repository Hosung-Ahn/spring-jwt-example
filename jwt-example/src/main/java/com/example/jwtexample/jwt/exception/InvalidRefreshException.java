package com.example.jwtexample.jwt.exception;

public class InvalidRefreshException extends RuntimeException{
    public InvalidRefreshException(String message) {
        super(message);
    }
}
