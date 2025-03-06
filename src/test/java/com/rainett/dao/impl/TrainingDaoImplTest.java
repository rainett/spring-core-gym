package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainingDaoImplTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private TrainingDaoImpl trainingDao;

    private Map<Long, Object> trainingMap;

    @BeforeEach
    void setUp() {
        trainingMap = new HashMap<>();
        when(dataStorage.getNamespace("trainings")).thenReturn(trainingMap);
    }

    @Test
    void savesTraining() {
        Training training = new Training();
        training.setId(1L);
        training.setName("High intensity interval training");

        trainingDao.save(training);
        assertEquals(training, trainingMap.get(1L));
    }

    @Test
    void findsTrainingById() {
        Training training = new Training();
        training.setId(2L);
        training.setName("Low intensity training");
        trainingMap.put(2L, training);

        Training foundTraining = trainingDao.findById(2L);
        assertEquals(training, foundTraining);
    }
}