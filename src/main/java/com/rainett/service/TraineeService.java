package com.rainett.service;

import com.rainett.dto.trainee.CreateTraineeProfileRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.dto.user.UsernameRequest;
import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.trainee.UpdateTraineeRequest;
import com.rainett.dto.trainee.UpdateTraineeTrainersRequest;
import com.rainett.model.Trainee;

public interface TraineeService {
    Trainee createProfile(CreateTraineeProfileRequest request);

    Trainee findByUsername(UsernameRequest request);

    Trainee updatePassword(UpdatePasswordRequest request);

    Trainee updateTrainee(UpdateTraineeRequest request);

    Trainee setActiveStatus(UpdateUserActiveRequest request);

    void deleteProfile(UsernameRequest request);

    Trainee updateTrainers(UpdateTraineeTrainersRequest request);
}
