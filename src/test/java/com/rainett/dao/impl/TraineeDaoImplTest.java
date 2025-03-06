package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

import com.rainett.model.Trainee;
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
class TraineeDaoImplTest {
    @Mock
    private DataStorage dataStorage;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    private Map<Long, Object> traineeStorage;

    @BeforeEach
    void setUp() {
        traineeStorage = new HashMap<>();
    }

    @Test
    void testSaveTrainee() {
        when(dataStorage.getNamespace("trainees")).thenReturn(traineeStorage);
        Trainee trainee = new Trainee();
        trainee.setUserId(1L);
        trainee.setUsername("John Doe");
        traineeDao.save(trainee);
        assertEquals(trainee, traineeStorage.get(1L));
    }

    @Test
    void testFindByUserId() {
        when(dataStorage.getNamespace("trainees")).thenReturn(traineeStorage);
        Trainee trainee = new Trainee();
        trainee.setUserId(2L);
        trainee.setUsername("Jane Doe");
        traineeStorage.put(2L, trainee);

        Trainee foundTrainee = traineeDao.findByUserId(2L);
        assertNotNull(foundTrainee);
        assertEquals("Jane Doe", foundTrainee.getUsername());
    }

    @Test
    void testDeleteByUserId() {
        when(dataStorage.getNamespace("trainees")).thenReturn(traineeStorage);
        Trainee trainee = new Trainee();
        trainee.setUserId(3L);
        trainee.setUsername("Mike Smith");
        traineeStorage.put(3L, trainee);

        traineeDao.deleteByUserId(3L);
        assertNull(traineeStorage.get(3L));
    }

    @Test
    void testUsernameExists() {
        when(traineeDao.usernameExists("John Doe")).thenReturn(true);
        assertTrue(traineeDao.usernameExists("John Doe"));
        assertFalse(traineeDao.usernameExists("Jane Doe"));
    }
}