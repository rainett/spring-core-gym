package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.service.UserService;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerServiceImplTest {
    @Mock
    private TrainerDao trainerDao;

    @Mock
    private UserService userService;

    @InjectMocks
    private TrainerServiceImpl trainerService;

    @Test
    void testCreateProfile() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("Jane");
        trainer.setLastName("Doe");

        when(userService.generateUniqueUsername("Jane", "Doe")).thenReturn("jane.doe");
        when(userService.generateRandomPassword()).thenReturn("abcdefghij");
        when(trainerDao.save(trainer)).thenReturn(trainer);

        Trainer result = trainerService.createProfile(trainer);

        assertEquals("jane.doe", trainer.getUsername(), "Username should be set to 'jane.doe'");
        assertEquals("abcdefghij", trainer.getPassword(), "Password should be set to 'abcdefghij'");
        verify(userService, times(1)).generateUniqueUsername("Jane", "Doe");
        verify(userService, times(1)).generateRandomPassword();
        verify(trainerDao, times(1)).save(trainer);
        assertEquals(trainer, result, "The created profile should match the saved trainer");
    }

    @Test
    void testUpdateProfile_Success() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        when(trainerDao.findByUserId(1L)).thenReturn(trainer);
        when(trainerDao.save(trainer)).thenReturn(trainer);

        Trainer result = trainerService.updateProfile(trainer);

        verify(trainerDao, times(1)).findByUserId(1L);
        verify(trainerDao, times(1)).save(trainer);
        assertEquals(trainer, result, "Updated profile should match the saved trainer");
    }

    @Test
    void testUpdateProfile_NotFound() {
        Trainer trainer = new Trainer();
        trainer.setUserId(2L);
        when(trainerDao.findByUserId(2L)).thenReturn(null);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            trainerService.updateProfile(trainer);
        });
        assertEquals("Trainee not found", exception.getMessage(), "Expected error message for non-existent trainer");
        verify(trainerDao, times(1)).findByUserId(2L);
        verify(trainerDao, times(0)).save(trainer);
    }

    @Test
    void testGetProfile() {
        Trainer trainer = new Trainer();
        when(trainerDao.findByUserId(1L)).thenReturn(trainer);

        Trainer result = trainerService.getProfile(1L);

        verify(trainerDao, times(1)).findByUserId(1L);
        assertEquals(trainer, result, "getProfile should return the expected trainer");
    }

    @Test
    void testGetAll() {
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
        when(trainerDao.findAll()).thenReturn(trainers);

        List<Trainer> result = trainerService.getAll();

        verify(trainerDao, times(1)).findAll();
        assertEquals(trainers, result, "getAll should return all trainers");
    }
}
