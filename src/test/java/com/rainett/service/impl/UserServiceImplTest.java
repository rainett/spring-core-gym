package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.rainett.storage.DataStorage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGeneratesRandomPassword() {
        String generatedPassword = userService.generateRandomPassword();
        assertNotNull(generatedPassword);
        assertEquals(10, generatedPassword.length());
        assertNotEquals(generatedPassword, userService.generateRandomPassword());
    }

    @Test
    void testGeneratesUniqueUsername() {
        when(dataStorage.usernameExists(anyString())).thenReturn(true);
        when(dataStorage.usernameExists("John.Doe.13")).thenReturn(false);
        String generatedUsername = userService.generateUniqueUsername("John", "Doe");
        assertEquals("John.Doe.13", generatedUsername);
    }
}