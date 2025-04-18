package com.rainett.service.impl;

import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.model.User;
import com.rainett.security.service.JwtService;
import com.rainett.service.CredentialsGenerationService;
import com.rainett.service.ProfileCreationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileCreationServiceImpl<T extends User> implements ProfileCreationService<T> {
    private final CredentialsGenerationService credentialsGenerationService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserCredentialsResponse createProfile(T user, JpaRepository<T, Long> repository) {
        credentialsGenerationService.createCredentials(user);
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user = repository.save(user);
        String username = user.getUsername();
        String token = jwtService.generateToken(user);
        return new UserCredentialsResponse(username, password, token);
    }
}
