package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.service.UserService;
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
    void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setFirstName("John");
        trainer.setLastName("Doe");
        trainerService.createProfile(trainer);
        verify(userService, times(1)).generateUniqueUsername(anyString(), anyString());
        verify(userService, times(1)).generateRandomPassword();
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void testUpdateTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        when(trainerDao.findByUserId(1L)).thenReturn(trainer);
        trainerService.updateProfile(trainer);
        verify(trainerDao, times(1)).save(trainer);
    }

    @Test
    void testUpdateTrainer_notFound() {
        when(trainerDao.findByUserId(1L)).thenReturn(null);
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        assertThrows(IllegalArgumentException.class, () -> trainerService.updateProfile(trainer));
    }

    @Test
    void testGetTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        when(trainerDao.findByUserId(1L)).thenReturn(trainer);
        Trainer profile = trainerService.getProfile(1L);
        verify(trainerDao, times(1)).findByUserId(1L);
        assertEquals(trainer, profile);
    }
}