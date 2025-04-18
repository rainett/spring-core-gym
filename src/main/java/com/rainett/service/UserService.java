package com.rainett.service;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;

public interface UserService {
    String login(LoginRequest request);

    void updatePassword(String username, UpdatePasswordRequest request);

    void updateStatus(String username, UpdateUserActiveRequest request);

    void logout(String asda);
}
