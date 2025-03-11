package com.rainett.exceptions;

public class DataLoadException extends RuntimeException {
    public DataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
