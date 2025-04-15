package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.rainett.exceptions.LoginException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImplTest {
    @InjectMocks
    private AuthenticationServiceImpl service;

    @Mock
    private UserRepository repository;

    @Test
    @DisplayName("Authenticates user successfully")
    void testAuthentication_success() {
        String username = "john.doe";
        String password = "realPassword";
        User user = new User();
        user.setUsername(username);
        user.setPassword(password);

        when(repository.findByUsername(username)).thenReturn(Optional.of(user));

        assertDoesNotThrow(() -> service.authenticate(username, password));
    }

    @Test
    @DisplayName("Auth fails when user not found")
    void testAuthentication_fail_notFound() {
        when(repository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(LoginException.class, () -> service.authenticate("nobody", "it doesn't matter"));
    }

    @Test
    @DisplayName("Auth fails when passwords don't match")
    void testAuthentication_fail_passwordsDontMatch() {
        String username = "john.doe";
        User user = new User();
        user.setUsername(username);
        user.setPassword("realPassword");

        when(repository.findByUsername(username)).thenReturn(Optional.of(user));

        assertThrows(LoginException.class, () -> service.authenticate(username, "it doesn't matter"));
    }
}