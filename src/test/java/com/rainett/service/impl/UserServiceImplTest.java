package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.security.service.BruteForceProtectionService;
import com.rainett.security.service.JwtService;
import com.rainett.security.service.TokenBlacklistService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private BruteForceProtectionService bruteForceProtectionService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;
    @Mock
    private TokenBlacklistService tokenBlacklistService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Logins user")
    void testLogin() {
        User user = new User();
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.login(new LoginRequest("username", "password"));
        verify(userRepository, times(1))
                .findByUsername(anyString());
    }

    @Test
    @DisplayName("Update password throws an exception when user not found")
    void testUpdatePassword_throwsException_whenUserNotFound() {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setNewPassword("newPassword");
        User user = new User();
        String initialPassword = "password";
        user.setPassword(initialPassword);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updatePassword("", request));
        assertEquals(initialPassword, user.getPassword());
    }

    @Test
    @DisplayName("Updates password successfully")
    void testUpdatePassword_success() {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        User user = new User();
        String initialPassword = "password";
        user.setPassword(initialPassword);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        userService.updatePassword("", request);
        assertEquals(request.getNewPassword(), user.getPassword());
    }

    @Test
    @DisplayName("Update status throws an exception when user not found")
    void testUpdateStatus_throwsException_whenUserNotFound() {
        UpdateUserActiveRequest request = new UpdateUserActiveRequest(true);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class,
                () -> userService.updateStatus("", request));
    }

    @Test
    @DisplayName("Update status successfully")
    void testUpdateStatus_success() {
        UpdateUserActiveRequest request = new UpdateUserActiveRequest(true);
        User user = new User();
        user.setIsActive(false);
        user.setActiveUpdatedAt(LocalDateTime.now());
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        LocalDateTime beforeExecution = LocalDateTime.now();
        userService.updateStatus("", request);
        assertTrue(user.getIsActive());
        assertTrue(user.getActiveUpdatedAt().isAfter(beforeExecution));
    }
}
