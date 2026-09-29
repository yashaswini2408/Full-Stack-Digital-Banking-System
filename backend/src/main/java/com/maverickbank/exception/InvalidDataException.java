package com.maverickbank.exception;

/**
 * Exception thrown when invalid data is provided
 */
public class InvalidDataException extends RuntimeException {
    
    public InvalidDataException(String message) {
        super(message);
    }
    
    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
