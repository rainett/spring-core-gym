package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingServiceImplTest {
    @Mock
    private TrainingDao trainingDao;

    @InjectMocks
    private TrainingServiceImpl trainingService;

    @Test
    void testSaveTraining() {
        Training training = new Training();
        trainingService.createTraining(training);
        verify(trainingDao, times(1)).save(training);
    }

    @Test
    void testGetTraining() {
        Training training = new Training();
        training.setId(1L);
        when(trainingDao.findById(1L)).thenReturn(training);
        Training receivedTraining = trainingService.getTraining(1L);
        assertEquals(training, receivedTraining);
    }
}