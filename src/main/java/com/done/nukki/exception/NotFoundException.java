package com.done.nukki.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message + " Not Found");
    }

    public NotFoundException(String message, Throwable cause) {
        super(message + " Not Found");
    }
}