package com.rainett.exceptions;

public class IdNotFoundException extends RuntimeException {
    public IdNotFoundException(String simpleClassName) {
        super("Id annotation not found on " + simpleClassName);
    }
}
