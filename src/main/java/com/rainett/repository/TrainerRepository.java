package com.rainett.repository;

import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.model.Trainer;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TrainerRepository extends GenericRepository<Trainer> {
    List<Trainer> findByUsernames(List<String> usernames);

    Optional<Trainer> findByUsername(String username);

    Optional<TrainerResponse> findTrainerDtoByUsername(String username);

    List<TrainerTrainingResponse> findTrainerTrainingsDto(String username, LocalDate from,
                                                          LocalDate to, String traineeUsername);
}
