package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void testCreateProfile() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        when(userService.generateUniqueUsername("John", "Doe")).thenReturn("john.doe");
        when(userService.generateRandomPassword()).thenReturn("abcdefghij");
        when(traineeDao.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.createProfile(trainee);

        assertEquals("john.doe", trainee.getUsername(), "Username should be set to 'john.doe'");
        assertEquals("abcdefghij", trainee.getPassword(), "Password should be set to 'abcdefghij'");
        verify(userService, times(1)).generateUniqueUsername("John", "Doe");
        verify(userService, times(1)).generateRandomPassword();
        verify(traineeDao, times(1)).save(trainee);
        assertEquals(trainee, result, "The created profile should match the saved trainee");
    }

    @Test
    void testUpdateProfile_Success() {
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        when(traineeDao.findByUserId(1L)).thenReturn(trainee);
        when(traineeDao.save(trainee)).thenReturn(trainee);

        Trainee result = traineeService.updateProfile(trainee);

        verify(traineeDao, times(1)).findByUserId(1L);
        verify(traineeDao, times(1)).save(trainee);
        assertEquals(trainee, result, "Updated profile should match the saved trainee");
    }

    @Test
    void testUpdateProfile_NotFound() {
        Trainee trainee = new Trainee();
        trainee.setUserId(2L);
        when(traineeDao.findByUserId(2L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            traineeService.updateProfile(trainee);
        });
        assertEquals("Trainee not found", exception.getMessage(), "Expected error message for non-existent trainee");
        verify(traineeDao, times(1)).findByUserId(2L);
        verify(traineeDao, times(0)).save(trainee);
    }

    @Test
    void testDeleteProfile() {
        Long userId = 1L;
        when(traineeDao.deleteByUserId(userId)).thenReturn(new Trainee());

        traineeService.deleteProfile(userId);

        verify(traineeDao, times(1)).deleteByUserId(userId);
    }

    @Test
    void testGetProfile() {
        Long userId = 1L;
        Trainee trainee = new Trainee();
        when(traineeDao.findByUserId(userId)).thenReturn(trainee);

        Trainee result = traineeService.getProfile(userId);

        verify(traineeDao, times(1)).findByUserId(userId);
        assertEquals(trainee, result, "getProfile should return the expected trainee");
    }

    @Test
    void testGetAll() {
        List<Trainee> trainees = Arrays.asList(new Trainee(), new Trainee());
        when(traineeDao.findAll()).thenReturn(trainees);

        List<Trainee> result = traineeService.getAll();

        verify(traineeDao, times(1)).findAll();
        assertEquals(trainees, result, "getAll should return all trainees");
    }
}
