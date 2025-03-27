package com.rainett.repository;

import com.rainett.dto.training.FindTraineeTrainingsRequest;
import com.rainett.dto.training.FindTrainerTrainingsRequest;
import com.rainett.model.Training;
import jakarta.validation.Valid;
import java.util.List;

public interface TrainingRepository extends GenericRepository<Training> {
    List<Training> findTraineeTrainings(@Valid FindTraineeTrainingsRequest request);

    List<Training> findTrainerTrainings(@Valid FindTrainerTrainingsRequest request);
}
