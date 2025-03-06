package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import com.rainett.model.Trainer;
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
class TrainerDaoImplTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private TrainerDaoImpl trainerDao;

    private Map<Long, Object> trainerStorage;

    @BeforeEach
    void setUp() {
        trainerStorage = new HashMap<>();
        when(dataStorage.getNamespace("trainers")).thenReturn(trainerStorage);
    }

    @Test
    void testSaveTrainer() {
        Trainer trainer = new Trainer();
        trainer.setUserId(1L);
        trainerDao.save(trainer);
        assertEquals(trainer, trainerStorage.get(1L));
    }

    @Test
    void testFindByUserId() {
        Trainer trainer = new Trainer();
        trainer.setUserId(2L);
        trainer.setUsername("John Doe");
        trainerStorage.put(2L, trainer);

        Trainer foundTrainer = trainerDao.findByUserId(2L);
        assertEquals(trainer, foundTrainer);
    }
}