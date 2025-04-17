package com.rainett.service;

public interface BruteForceProtectionService {
    void loginSuccess(String username);

    void loginFailure(String username);

    boolean isBlocked(String username);
}
