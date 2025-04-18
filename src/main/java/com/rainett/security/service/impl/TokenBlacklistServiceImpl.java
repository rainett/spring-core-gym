package com.rainett.security.service.impl;

import com.rainett.security.service.TokenBlacklistService;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    private final Set<String> tokenBlacklist = ConcurrentHashMap.newKeySet();

    @Override
    public void revokeToken(String token) {
        tokenBlacklist.add(token);
    }

    @Override
    public boolean isTokenRevoked(String token) {
        return tokenBlacklist.contains(token);
    }
}
