package org.example.exceptions;

public class EntityNotFoundException extends NoSuchElementFoundException {
    public EntityNotFoundException(String message){
        super(message);
    }

    public EntityNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
