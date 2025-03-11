package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {
    @Mock
    private DataStorage<Training> dataStorage;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    @Test
    void testSaveTraining() {
        Training training = new Training();
        when(dataStorage.save(training)).thenReturn(training);

        Training saved = trainingDao.save(training);

        verify(dataStorage, times(1)).save(training);
        assertEquals(training, saved, "Saved training should match the one provided");
    }

    @Test
    void testFindById() {
        Long id = 3L;
        Training training = new Training();
        when(dataStorage.findById(Training.class, id)).thenReturn(training);

        Training found = trainingDao.findById(id);

        verify(dataStorage, times(1)).findById(Training.class, id);
        assertEquals(training, found, "Found training should match expected");
    }

    @Test
    void testFindAll() {
        List<Training> trainings = Arrays.asList(new Training(), new Training());
        when(dataStorage.findAll(Training.class)).thenReturn(trainings);

        List<Training> result = trainingDao.findAll();

        verify(dataStorage, times(1)).findAll(Training.class);
        assertEquals(trainings, result, "findAll should return all trainings");
    }
}