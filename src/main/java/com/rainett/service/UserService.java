package com.rainett.service;

public interface UserService {
    String generateUniqueUsername(String firstName, String lastName);

    String generateRandomPassword();
}
