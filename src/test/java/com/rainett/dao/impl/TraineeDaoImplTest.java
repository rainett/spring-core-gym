package com.rainett.dao.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.rainett.model.Trainee;
import com.rainett.storage.DataStorage;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TraineeDaoImplTest {

    @Mock
    private DataStorage<Trainee> dataStorage;

    @InjectMocks
    private TraineeDaoImpl traineeDao;

    @Test
    void testSaveTrainee() {
        Trainee trainee = new Trainee();
        when(dataStorage.save(trainee)).thenReturn(trainee);

        Trainee saved = traineeDao.save(trainee);

        verify(dataStorage, times(1)).save(trainee);
        assertEquals(trainee, saved, "Saved trainee should match the one provided");
    }

    @Test
    void testFindByUserId() {
        Long userId = 1L;
        Trainee trainee = new Trainee();
        when(dataStorage.findById(Trainee.class, userId)).thenReturn(trainee);

        Trainee found = traineeDao.findByUserId(userId);

        verify(dataStorage, times(1)).findById(Trainee.class, userId);
        assertEquals(trainee, found, "Found trainee should match expected");
    }

    @Test
    void testDeleteByUserId() {
        Long userId = 1L;
        Trainee trainee = new Trainee();
        when(dataStorage.findById(Trainee.class, userId)).thenReturn(trainee);
        when(dataStorage.delete(trainee)).thenReturn(trainee);

        Trainee deleted = traineeDao.deleteByUserId(userId);

        verify(dataStorage, times(1)).findById(Trainee.class, userId);
        verify(dataStorage, times(1)).delete(trainee);
        assertEquals(trainee, deleted, "Deleted trainee should match expected");
    }

    @Test
    void testFindAll() {
        List<Trainee> trainees = Arrays.asList(new Trainee(), new Trainee());
        when(dataStorage.findAll(Trainee.class)).thenReturn(trainees);

        List<Trainee> result = traineeDao.findAll();

        verify(dataStorage, times(1)).findAll(Trainee.class);
        assertEquals(trainees, result, "findAll should return all trainees");
    }
}
