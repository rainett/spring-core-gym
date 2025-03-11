package com.rainett.service.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.service.TrainingService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDao trainingDao;

    @Override
    public Training createTraining(Training training) {
        log.info("Saving training: {}", training);
        return trainingDao.save(training);
    }

    @Override
    public Training getTraining(Long id) {
        log.info("Getting training by id: {}", id);
        return trainingDao.findById(id);
    }

    @Override
    public List<Training> getAll() {
        log.info("Getting all trainings");
        return trainingDao.findAll();
    }
}
