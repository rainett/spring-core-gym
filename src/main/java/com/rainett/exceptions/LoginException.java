package com.rainett.exceptions;

public class LoginException extends RuntimeException {
    public LoginException() {
        super("Invalid username or password");
    }
}
