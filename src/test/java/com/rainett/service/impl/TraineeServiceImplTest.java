package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeServiceImplTest {
    @Mock
    private TraineeDao traineeDao;

    @InjectMocks
    private TraineeServiceImpl traineeService;

    @Test
    void createsProfile() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeService.createProfile(trainee);

        verify(traineeDao, times(1)).save(trainee);
        assertEquals("John.Doe", trainee.getUsername());
        assertEquals(10, trainee.getPassword().length());
    }

    @Test
    void createsProfileWithExistingUsername() {
        when(traineeDao.usernameExists(anyString())).thenReturn(true);
        when(traineeDao.usernameExists("John.Doe.12")).thenReturn(false);

        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeService.createProfile(trainee);

        verify(traineeDao, times(1)).save(trainee);
        assertEquals("John.Doe.12", trainee.getUsername());
    }

    @Test
    void testFindByUserId() {
        Trainee trainee = new Trainee();
        trainee.setUserId(2L);
        trainee.setUsername("Jane Doe");
        when(traineeDao.findByUserId(2L)).thenReturn(trainee);

        Trainee profile = traineeService.getProfile(2L);
        verify(traineeDao, times(1)).findByUserId(2L);
        assertEquals(trainee, profile);
    }

    @Test
    void testDeleteByUserId() {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        trainee.setUsername("Mike Smith");
        doNothing().when(traineeDao).deleteByUserId(3L);
        traineeService.deleteProfile(3L);
        verify(traineeDao, times(1)).deleteByUserId(3L);
    }

    @Test
    void updateProfile() {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        trainee.setUsername("Mike Smith");
        when(traineeDao.findByUserId(3L)).thenReturn(trainee);
        traineeService.updateProfile(trainee);
        verify(traineeDao, times(1)).save(trainee);

        when(traineeDao.findByUserId(3L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> traineeService.updateProfile(trainee));
    }
}