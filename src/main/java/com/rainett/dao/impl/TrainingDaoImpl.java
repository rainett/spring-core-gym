package com.rainett.dao.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.storage.DataStorage;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingDaoImpl implements TrainingDao {
    @Autowired
    private DataStorage<Training> dataStorage;

    @Override
    public Training save(Training training) {
        log.info("Saving training: {}", training);
        return dataStorage.save(training);
    }

    @Override
    public Training findById(Long id) {
        log.info("Finding training by id: {}", id);
        return dataStorage.findById(Training.class, id);
    }

    @Override
    public List<Training> findAll() {
        log.info("Finding all trainings");
        return dataStorage.findAll(Training.class);
    }
}
