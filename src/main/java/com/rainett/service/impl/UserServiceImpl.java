package com.rainett.service.impl;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.LoginException;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.exceptions.TooManyLoginAttemptsException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.security.service.JwtService;
import com.rainett.security.service.BruteForceProtectionService;
import com.rainett.security.service.TokenBlacklistService;
import com.rainett.service.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final BruteForceProtectionService bruteForceProtectionService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final TokenBlacklistService tokenBlacklistService;

    @Override
    @Transactional(readOnly = true)
    public String login(LoginRequest request) {
        User user = authenticate(request.getUsername(), request.getPassword());
        return jwtService.generateToken(user);
    }

    @Override
    @Transactional
    public void updatePassword(String username, UpdatePasswordRequest request) {
        User user = getUser(username);
        authenticate(username, request.getOldPassword());
        user.setPassword(request.getNewPassword());
    }

    @Override
    @Transactional
    public void updateStatus(String username, UpdateUserActiveRequest request) {
        User user = getUser(username);
        user.setIsActive(request.getIsActive());
        user.setActiveUpdatedAt(LocalDateTime.now());
    }

    @Override
    public void logout(String token) {
        tokenBlacklistService.revokeToken(token);
    }

    private User authenticate(String username, String password) {
        if (bruteForceProtectionService.isBlocked()) {
            throw new TooManyLoginAttemptsException();
        }
        User user = userRepository.findByUsername(username)
                .filter(u -> passwordEncoder.matches(password, u.getPassword()))
                .orElseThrow(() -> {
                    bruteForceProtectionService.loginFailure();
                    return new LoginException();
                });
        bruteForceProtectionService.loginSuccess();
        return user;
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found for username = [" + username + "]"));
    }
}
