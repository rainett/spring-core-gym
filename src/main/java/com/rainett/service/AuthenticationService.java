package com.rainett.service;

public interface AuthenticationService {
    boolean match(String identity, String password);
}
