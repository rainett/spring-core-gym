package com.rainett.dao.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.storage.DataStorage;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainerDaoImpl implements TrainerDao {
    private static final String NAMESPACE = "trainers";
    private static final AtomicLong ID_GENERATOR = new AtomicLong();

    @Autowired
    private DataStorage dataStorage;

    @Override
    public void save(Trainer trainer) {
        Long userId = Optional.ofNullable(trainer.getUserId())
                .orElseGet(ID_GENERATOR::getAndIncrement);
        dataStorage.getNamespace(NAMESPACE).put(userId, trainer);
        dataStorage.addUsername(trainer.getUsername(), userId);
    }

    @Override
    public Trainer findByUserId(Long userId) {
        Map<Long, Object> trainers = dataStorage.getNamespace(NAMESPACE);
        return (Trainer) trainers.get(userId);
    }
}
