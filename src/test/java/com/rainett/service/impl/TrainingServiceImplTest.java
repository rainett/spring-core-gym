package com.rainett.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import java.util.Arrays;
import java.util.List;
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
    void testCreateTraining() {
        Training training = new Training();
        when(trainingDao.save(training)).thenReturn(training);

        Training result = trainingService.createTraining(training);

        verify(trainingDao, times(1)).save(training);
        assertEquals(training, result, "The created training should match the saved training");
    }

    @Test
    void testGetTraining() {
        Long id = 1L;
        Training training = new Training();
        when(trainingDao.findById(id)).thenReturn(training);

        Training result = trainingService.getTraining(id);

        verify(trainingDao, times(1)).findById(id);
        assertEquals(training, result, "The retrieved training should match the expected training");
    }

    @Test
    void testGetAll() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(trainingDao.findAll()).thenReturn(trainings);

        List<Training> result = trainingService.getAll();

        verify(trainingDao, times(1)).findAll();
        assertEquals(trainings, result, "getAll should return all trainings");
    }
}
