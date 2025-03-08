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
import com.rainett.service.UserService;
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
    void createsProfile() {
        Trainee trainee = new Trainee();
        trainee.setFirstName("John");
        trainee.setLastName("Doe");
        traineeService.createProfile(trainee);
        verify(userService, times(1)).generateUniqueUsername(anyString(), anyString());
        verify(userService, times(1)).generateRandomPassword();
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void testFindByUserId() {
        Trainee trainee = new Trainee();
        trainee.setUserId(2L);
        when(traineeDao.findByUserId(2L)).thenReturn(trainee);

        Trainee profile = traineeService.getProfile(2L);
        verify(traineeDao, times(1)).findByUserId(2L);
        assertEquals(trainee, profile);
    }

    @Test
    void testDeleteByUserId() {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        doNothing().when(traineeDao).deleteByUserId(3L);
        traineeService.deleteProfile(3L);
        verify(traineeDao, times(1)).deleteByUserId(3L);
    }

    @Test
    void updateProfile() {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        when(traineeDao.findByUserId(3L)).thenReturn(trainee);
        traineeService.updateProfile(trainee);
        verify(traineeDao, times(1)).save(trainee);
    }

    @Test
    void testUpdateProfile_notFound() {
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        when(traineeDao.findByUserId(3L)).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> traineeService.updateProfile(trainee));
    }
}