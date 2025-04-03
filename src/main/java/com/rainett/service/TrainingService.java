package com.rainett.service;

import com.rainett.dto.training.CreateTrainingRequest;
import com.rainett.dto.training.FindTraineeTrainingsRequest;
import com.rainett.dto.training.FindTrainerTrainingsRequest;
import com.rainett.model.Training;
import java.util.List;

public interface TrainingService {
    Training createTraining(CreateTrainingRequest request);

    List<Training> findTrainingsForTrainee(FindTraineeTrainingsRequest request);

    List<Training> findTrainingsForTrainer(FindTrainerTrainingsRequest request);
}
