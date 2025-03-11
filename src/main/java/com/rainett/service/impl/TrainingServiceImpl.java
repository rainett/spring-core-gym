package com.rainett.service.impl;

import com.rainett.dao.TrainingDao;
import com.rainett.model.Training;
import com.rainett.service.TrainingService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TrainingServiceImpl implements TrainingService {
    @Autowired
    private TrainingDao trainingDao;

    @Override
    public Training createTraining(Training training) {
        return trainingDao.save(training);
    }

    @Override
    public Training getTraining(Long id) {
        return trainingDao.findById(id);
    }

    @Override
    public List<Training> getAll() {
        return trainingDao.findAll();
    }
}
