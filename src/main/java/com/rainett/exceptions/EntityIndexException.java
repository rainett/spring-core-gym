package com.rainett.exceptions;

public class EntityIndexException extends RuntimeException {
    public EntityIndexException(String message, IllegalAccessException e) {
        super(message, e);
    }
}
