package com.rainett.repository;

import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainerDto;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import com.rainett.model.Trainee;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TraineeRepository extends GenericRepository<Trainee> {
    Optional<TraineeResponse> findTraineeDtoByUsername(String username);

    List<TraineeTrainingsResponse> findTraineeTrainingsDto(String username,
                                                           LocalDate from,
                                                           LocalDate to,
                                                           String trainerUsername,
                                                           String trainingType);

    List<TraineeTrainerDto> findUnassignedTrainersDto(String username);

    Optional<Trainee> findByUsername(String username);
}
