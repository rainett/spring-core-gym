package com.rainett.dao.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.storage.DataStorage;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TraineeDaoImpl implements TraineeDao {
    private static final String NAMESPACE = "trainees";
    private static final AtomicLong ID_GENERATOR = new AtomicLong();
    private final DataStorage dataStorage;

    @Override
    public void save(Trainee trainee) {
        Long userId = Optional.ofNullable(trainee.getUserId())
                .orElseGet(ID_GENERATOR::getAndIncrement);
        dataStorage.getNamespace(NAMESPACE).put(userId, trainee);
        dataStorage.addUsername(trainee.getUsername(), userId);
    }

    @Override
    public Trainee findByUserId(Long userId) {
        Map<Long, Object> trainees = dataStorage.getNamespace(NAMESPACE);
        return (Trainee) trainees.get(userId);
    }

    @Override
    public void deleteByUserId(Long userId) {
        Map<Long, Object> trainees = dataStorage.getNamespace(NAMESPACE);
        Trainee trainee = (Trainee) trainees.remove(userId);
        dataStorage.removeUsername(trainee.getUsername());
    }
}
