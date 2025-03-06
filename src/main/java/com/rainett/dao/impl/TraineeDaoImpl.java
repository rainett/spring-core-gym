package com.rainett.dao.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.storage.DataStorage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    private static final String NAMESPACE = "trainees";
    private final DataStorage dataStorage;

    @Override
    public void save(Trainee trainee) {
        Long userId = trainee.getUserId();
        dataStorage.getNamespace(NAMESPACE).put(userId, trainee);
    }

    @Override
    public Trainee findByUserId(Long userId) {
        Map<Long, Object> trainees = dataStorage.getNamespace(NAMESPACE);
        return (Trainee) trainees.get(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        Map<Long, Object> trainees = dataStorage.getNamespace(NAMESPACE);
        trainees.remove(userId);
    }
}
