package org.example.exceptions;

public class NoSuchElementFoundException extends RuntimeException {
    public NoSuchElementFoundException(){
        this("No such element found.");
    }

    public NoSuchElementFoundException(String message){
        super(message);
    }

    public NoSuchElementFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
