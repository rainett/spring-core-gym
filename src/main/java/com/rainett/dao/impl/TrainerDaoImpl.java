package com.rainett.dao.impl;

import com.rainett.dao.TrainerDao;
import com.rainett.model.Trainer;
import com.rainett.storage.DataStorage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainerDaoImpl implements TrainerDao {
    @Autowired
    private DataStorage<Trainer> dataStorage;

    @Override
    public Trainer save(Trainer trainer) {
        log.info("Saving trainer: {}", trainer);
        return dataStorage.save(trainer);
    }

    @Override
    public Trainer findByUserId(Long userId) {
        log.info("Finding trainer by userId: {}", userId);
        return dataStorage.findById(Trainer.class, userId);
    }

    @Override
    public List<Trainer> findAll() {
        log.info("Finding all trainers");
        return dataStorage.findAll(Trainer.class);
    }
}
