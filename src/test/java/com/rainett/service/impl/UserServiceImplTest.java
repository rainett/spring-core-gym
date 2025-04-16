package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dto.user.LoginRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.exceptions.LoginException;
import com.rainett.exceptions.ResourceNotFoundException;
import com.rainett.model.User;
import com.rainett.repository.UserRepository;
import com.rainett.service.AuthenticationService;
import java.time.LocalDateTime;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    @DisplayName("Logins user")
    void testLogin() {
        userService.login(new LoginRequest("username", "password"));
        verify(authenticationService, times(1))
                .authenticate(anyString(), anyString());
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
    @DisplayName("Update password throws an exception when user not authenticated")
    void testUpdatePassword_throwsException_whenUserNotAuthenticated() {
        UpdatePasswordRequest request = new UpdatePasswordRequest();
        request.setOldPassword("oldPassword");
        request.setNewPassword("newPassword");
        User user = new User();
        String initialPassword = "password";
        user.setPassword(initialPassword);
        when(userRepository.findByUsername(anyString())).thenReturn(Optional.of(user));
        doThrow(new LoginException()).when(authenticationService)
                .authenticate(anyString(), anyString());
        assertThrows(LoginException.class,
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
        doNothing().when(authenticationService).authenticate(anyString(), anyString());
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
