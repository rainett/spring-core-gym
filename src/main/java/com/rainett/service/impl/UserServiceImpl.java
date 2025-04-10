package com.rainett.service.impl;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.AuthenticationService;
import com.rainett.service.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final AuthenticationService authenticationService;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void login(LoginRequest request) {
        authenticationService.authenticate(request.getUsername(), request.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(String username, UpdatePasswordRequest request) {
        User user = getUser(username);
        authenticationService.authenticate(user.getUsername(), request.getOldPassword());
        user.setPassword(request.getNewPassword());
    }

    @Override
    @Transactional
    public void updateStatus(String username, UpdateUserActiveRequest request) {
        User user = getUser(username);
        user.setIsActive(request.getIsActive());
        user.setActiveUpdatedAt(LocalDateTime.now());
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found for username = [" + username + "]"));
    }
}
