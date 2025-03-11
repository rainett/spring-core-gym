package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.model.Trainee;
import com.rainett.model.Trainer;
import com.rainett.model.User;
import com.rainett.storage.DataStorage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private DataStorage<User> dataStorage;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void testGenerateUniqueUsername_NoConflict() {
        when(dataStorage.findAll(Trainer.class)).thenReturn(Collections.emptyList());
        when(dataStorage.findAll(Trainee.class)).thenReturn(Collections.emptyList());

        String username = userService.generateUniqueUsername("john", "doe");

        assertEquals("john.doe", username, "Username should be generated as 'john.doe'");
        verify(dataStorage, times(1)).findAll(Trainer.class);
        verify(dataStorage, times(1)).findAll(Trainee.class);
    }

    @Test
    void testGenerateUniqueUsername_WithConflict() {
        Trainer trainer = org.mockito.Mockito.mock(Trainer.class);
        when(trainer.getUsername()).thenReturn("john.doe");

        List<User> users = new ArrayList<>();
        users.add(trainer);
        when(dataStorage.findAll(Trainer.class)).thenReturn(users);
        when(dataStorage.findAll(Trainee.class)).thenReturn(Collections.emptyList());

        String username = userService.generateUniqueUsername("john", "doe");

        assertEquals("john.doe.1", username,
                "Username should be generated as 'john.doe.1' when conflict exists");
        verify(dataStorage, times(2)).findAll(Trainer.class);
        verify(dataStorage, times(2)).findAll(Trainee.class);
    }

    @Test
    void testGenerateRandomPassword() {
        String password = userService.generateRandomPassword();

        assertNotNull(password, "Generated password should not be null");
        assertEquals(10, password.length(), "Password should be 10 characters long");
    }
}
