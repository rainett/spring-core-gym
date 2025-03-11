package com.rainett.exceptions;

public class EntityIdException extends RuntimeException {
    public EntityIdException(String message, IllegalAccessException e) {
        super(message, e);
    }
}
