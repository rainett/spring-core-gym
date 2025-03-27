package com.rainett.service.impl;

import com.rainett.repository.UserRepository;
import com.rainett.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    @Override
    public boolean match(String identity, String password) {
        log.info("Matching identity {}", identity);
        return userRepository.findByUsername(identity)
                .map(user -> user.getPassword().equals(password))
                .orElse(false);
    }
}
