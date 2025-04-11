package com.rainett.service;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.dto.trainee.TraineeTrainingsResponse;
import java.time.LocalDate;
import java.util.List;

public interface TraineeService {
    TraineeResponse findByUsername(String username);

    List<TraineeTrainingsResponse> findTrainings(String username,
                                                 LocalDate from,
                                                 LocalDate to,
                                                 String trainerUsername,
                                                 String trainingType);

    List<TrainerDto> findUnassignedTrainers(String username);

    UserCredentialsResponse createProfile(CreateTraineeRequest request);

    TraineeResponse updateTrainee(String username, UpdateTraineeRequest request);

    List<TrainerDto> updateTrainers(String username, UpdateTraineeTrainersRequest request);

    void deleteProfile(String username);
}
