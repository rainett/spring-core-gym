package com.rainett.service;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import jakarta.validation.Valid;

public interface UserService {
    void login(LoginRequest request);

    void updatePassword(String username, UpdatePasswordRequest request);

    void updateStatus(String username, UpdateUserActiveRequest request);
}
