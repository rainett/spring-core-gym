package com.rainett.service.impl;

import com.rainett.exceptions.LoginException;
import com.rainett.repository.UserRepository;
import com.rainett.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public void authenticate(String username, String password) throws LoginException {
        userRepository.findByUsername(username)
                .filter(user -> user.getPassword().equals(password))
                .orElseThrow(LoginException::new);
    }
}
