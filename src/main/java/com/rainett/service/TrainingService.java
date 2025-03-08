package com.rainett.service;

import com.rainett.model.Training;

public interface TrainingService {
    void createTraining(Training training);

    Training getTraining(Long id);
}
