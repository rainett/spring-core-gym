package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.model.Trainer;
import com.rainett.storage.DataStorage;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TrainerDaoImplTest {
    @Mock
    private DataStorage<Trainer> dataStorage;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    @Test
    void testSaveTrainer() {
        Trainer trainer = new Trainer();
        when(dataStorage.save(trainer)).thenReturn(trainer);

        Trainer saved = trainerDao.save(trainer);

        verify(dataStorage, times(1)).save(trainer);
        assertEquals(trainer, saved, "Saved trainer should match the one provided");
    }

    @Test
    void testFindByUserId() {
        Long userId = 2L;
        Trainer trainer = new Trainer();
        when(dataStorage.findById(Trainer.class, userId)).thenReturn(trainer);

        Trainer found = trainerDao.findByUserId(userId);

        verify(dataStorage, times(1)).findById(Trainer.class, userId);
        assertEquals(trainer, found, "Found trainer should match expected");
    }

    @Test
    void testFindAll() {
        List<Trainer> trainers = Arrays.asList(new Trainer(), new Trainer());
        when(dataStorage.findAll(Trainer.class)).thenReturn(trainers);

        List<Trainer> result = trainerDao.findAll();

        verify(dataStorage, times(1)).findAll(Trainer.class);
        assertEquals(trainers, result, "findAll should return all trainers");
    }
}