package com.rainett.service.impl;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.EntityNotFoundException;
import com.rainett.exceptions.LoginException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.UserService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void login(LoginRequest request) {
        login(request.getUsername(), request.getPassword());
    }

    @Override
    @Transactional
    public void updatePassword(String username, UpdatePasswordRequest request) {
        User user = login(username, request.getOldPassword());
        user.setPassword(request.getNewPassword());
    }

    @Override
    @Transactional
    public void updateStatus(String username, UpdateUserActiveRequest request) {
        User user = getUser(username);
        user.setIsActive(request.getIsActive());
        user.setActiveUpdatedAt(LocalDateTime.now());
    }

    private User login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(() -> new LoginException("Invalid username or password"));
    }

    private User getUser(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException(
                        "User not found for username = [" + username + "]"));
    }
}
