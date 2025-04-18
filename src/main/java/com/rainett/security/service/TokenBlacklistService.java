package com.rainett.security.service;

public interface TokenBlacklistService {
    void revokeToken(String token);

    boolean isTokenRevoked(String token);
}
