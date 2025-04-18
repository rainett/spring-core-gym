package com.rainett.security.service;

import com.rainett.model.User;
import com.rainett.security.TokenUserDto;

public interface JwtService {
    String generateToken(User user);

    TokenUserDto validateAndParse(String token);
}
