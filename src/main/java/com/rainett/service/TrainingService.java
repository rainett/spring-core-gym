package com.rainett.service;

import com.rainett.model.Training;

public interface TrainingService {
    Long createTraining(Training training);

    Training getTraining(Long id);
}
