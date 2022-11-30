package org.example.exceptions;

public class MappingException extends RuntimeException {

    public MappingException(){
        this("Cannot map one type into another.");
    }

    public MappingException(String message){
        super(message);
    }
    public MappingException(String message, Throwable throwable){
        super(message, throwable);
    }
}
