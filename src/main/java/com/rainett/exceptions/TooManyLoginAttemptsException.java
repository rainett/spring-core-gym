package com.rainett.exceptions;

public class TooManyLoginAttemptsException extends RuntimeException {
    public TooManyLoginAttemptsException() {
        super("Too many login attempts");
    }
}
