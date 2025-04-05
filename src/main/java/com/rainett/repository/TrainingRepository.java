package com.rainett.repository;

import com.rainett.model.Training;
import java.util.List;

public interface TrainingRepository extends GenericRepository<Training> {
    List<Training> findTraineeTrainings(FindTraineeTrainingsRequest request);

    List<Training> findTrainerTrainings(FindTrainerTrainingsRequest request);
}
