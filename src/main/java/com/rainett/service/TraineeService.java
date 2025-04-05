package com.rainett.service;

import com.rainett.dto.trainee.CreateTraineeRequest;
import com.rainett.dto.trainee.TraineeResponse;
import com.rainett.dto.trainee.TraineeTrainerDto;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.dto.user.UserCredentialsResponse;
import com.rainett.dto.user.UserTrainingsResponse;
import java.time.LocalDate;

public interface TraineeService {
    TraineeResponse findByUsername(String username);

    UserTrainingsResponse findTrainings(String username,
                                        LocalDate from,
                                        LocalDate to,
                                        String trainerUsername,
                                        String trainingType);

    TraineeTrainerDto findUnassignedTrainers(String username);

    UserCredentialsResponse createProfile(CreateTraineeRequest request);

    TraineeResponse updateTrainee(String username, UpdateTraineeRequest request);

    TraineeTrainerDto updateTrainers(String username, UpdateTraineeTrainersRequest request);

    void deleteProfile(String username);
}
