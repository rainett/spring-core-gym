package com.rainett.security.service.impl;

import com.rainett.security.service.BruteForceProtectionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class BruteForceProtectionServiceImpl implements BruteForceProtectionService {
    private static final String XF_HEADER = "X-Forwarded-For";

    private final Map<String, LoginAttempt> attempts = new ConcurrentHashMap<>();

    @Value("${security.brute-force.attempts:3}")
    private int maxAttempts;

    @Value("${security.brute-force.block-duration-ms:300000}") // default 5 minutes
    private long blockDurationMs;

    @Override
    public void loginSuccess() {
        String ip = getClientIp();
        if (ip != null) {
            attempts.remove(ip);
        }
    }

    @Override
    public void loginFailure() {
        String ip = getClientIp();
        if (ip == null) {
            return;
        }

        LoginAttempt attempt = attempts.computeIfAbsent(ip, k -> new LoginAttempt());
        attempt.failedCount++;
        if (attempt.failedCount >= maxAttempts) {
            attempt.lockExpiresAt = System.currentTimeMillis() + blockDurationMs;
        }
    }

    @Override
    public boolean isBlocked() {
        String ip = getClientIp();
        if (ip == null) {
            return false;
        }

        LoginAttempt attempt = attempts.get(ip);
        if (attempt == null || attempt.failedCount < maxAttempts) {
            return false;
        }

        long now = System.currentTimeMillis();
        if (now >= attempt.lockExpiresAt) {
            attempts.remove(ip);
            return false;
        }

        return true;
    }

    private static String getClientIp() {
        ServletRequestAttributes attrs =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs == null) {
            return null;
        }
        HttpServletRequest req = attrs.getRequest();
        String xf = req.getHeader(XF_HEADER);
        if (xf != null && !xf.isBlank()) {
            return xf.split(",")[0].trim();
        }
        return req.getRemoteAddr();
    }

    private static class LoginAttempt {
        int failedCount = 0;
        long lockExpiresAt = 0;
    }
}
