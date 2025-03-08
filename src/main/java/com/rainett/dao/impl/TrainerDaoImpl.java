package com.rainett.dao.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.storage.DataStorage;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainerDaoImpl implements TrainerDao {
    private static final String NAMESPACE = "trainers";
    private final DataStorage dataStorage;

    @Override
    public void save(Trainer trainer) {
        Long userId = trainer.getUserId();
        dataStorage.getNamespace(NAMESPACE).put(userId, trainer);
        dataStorage.addUsername(trainer.getUsername(), userId);
    }

    @Override
    public Trainer findByUserId(Long userId) {
        Map<Long, Object> trainers = dataStorage.getNamespace(NAMESPACE);
        return (Trainer) trainers.get(userId);
    }
}
