package com.rainett.security.service;

public interface BruteForceProtectionService {
    void loginSuccess();

    void loginFailure();

    boolean isBlocked();
}
