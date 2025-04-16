package com.rainett.service;

import com.rainett.exceptions.LoginException;

public interface AuthenticationService {
    void authenticate(String username, String password) throws LoginException;
}
