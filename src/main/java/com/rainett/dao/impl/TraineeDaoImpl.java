package com.rainett.dao.impl;

import com.rainett.dao.TraineeDao;
import com.rainett.model.Trainee;
import com.rainett.storage.DataStorage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TraineeDaoImpl implements TraineeDao {
    @Autowired
    private DataStorage<Trainee> dataStorage;

    @Override
    public Trainee save(Trainee trainee) {
        log.info("Saving trainee: {}", trainee);
        return dataStorage.save(trainee);
    }

    @Override
    public Trainee findByUserId(Long userId) {
        log.info("Finding trainee by userId: {}", userId);
        return dataStorage.findById(Trainee.class, userId);
    }

    @Override
    public Trainee deleteByUserId(Long userId) {
        log.info("Deleting trainee by userId: {}", userId);
        return dataStorage.delete(findByUserId(userId));
    }

    @Override
    public List<Trainee> findAll() {
        log.info("Finding all trainees");
        return dataStorage.findAll(Trainee.class);
    }
}
