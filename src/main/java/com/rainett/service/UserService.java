package com.rainett.service;

import com.rainett.model.User;

public interface UserService {
    boolean usernameRequiresUpdate(User user, UpdateUserRequest userDto);

    String generateUsername(String firstName, String lastName);

    String generatePassword();
}
