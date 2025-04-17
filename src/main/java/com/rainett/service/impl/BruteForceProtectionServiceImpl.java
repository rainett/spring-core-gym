package com.rainett.service.impl;

import com.rainett.service.BruteForceProtectionService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {
    private static final int MAX_ATTEMPTS = 3;
    private static final long BLOCK_DURATION_MS = 5L * 60 * 1000; // 5 minutes

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();


    @Override
    public void loginSuccess(String username) {
        attempts.remove(username);
    }

    @Override
    public void loginFailure(String username) {
        LoginAttempt attempt = attempts.computeIfAbsent(username, k -> new LoginAttempt());
        attempt.failedCount++;
        if (attempt.failedCount >= MAX_ATTEMPTS) {
            attempt.lockTime = System.currentTimeMillis() + BLOCK_DURATION_MS;
        }
    }

    @Override
    public boolean isBlocked(String username) {
        LoginAttempt attempt = attempts.get(username);
        if (attempt == null || attempt.failedCount < MAX_ATTEMPTS) {
            return false;
        }
        long elapsedTime = System.currentTimeMillis() - attempt.lockTime;
        if (elapsedTime > BLOCK_DURATION_MS) {
            attempts.remove(username);
            return false;
        }
        return true;
    }

    private static class LoginAttempt {
        int failedCount = 0;
        long lockTime = 0;
    }
}
