package com.rainett.service.impl;

import com.rainett.repository.UserRepository;
import com.rainett.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    @Override
    public boolean match(String identity, String password) {
        return userRepository.findByUsername(identity)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
