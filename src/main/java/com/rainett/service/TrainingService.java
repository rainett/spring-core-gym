package com.rainett.service;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.model.Training;

public interface TrainingService {
    Training createTraining(CreateTrainingRequest request);
}
