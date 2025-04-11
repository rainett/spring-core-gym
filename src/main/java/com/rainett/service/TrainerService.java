package com.rainett.service;

import com.rainett.dto.trainer.CreateTrainerRequest;
import com.rainett.dto.trainer.TrainerResponse;
import com.rainett.dto.trainer.TrainerTrainingResponse;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import java.time.LocalDate;
import java.util.List;

public interface TrainerService {
    TrainerResponse findByUsername(String username);

    List<TrainerTrainingResponse> findTrainings(String username,
                                               LocalDate from,
                                               LocalDate to,
                                               String traineeUsername);

    UserCredentialsResponse createProfile(CreateTrainerRequest request);

    TrainerResponse updateTrainer(String username, UpdateTrainerRequest request);
}
