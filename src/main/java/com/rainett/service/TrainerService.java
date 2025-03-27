package com.rainett.service;

import com.rainett.dto.user.UpdatePasswordRequest;
import com.rainett.dto.user.UpdateUserActiveRequest;
import com.rainett.dto.user.UsernameRequest;
import com.rainett.dto.trainer.CreateTrainerProfileRequest;
import com.rainett.dto.trainer.UpdateTrainerRequest;
import com.rainett.model.Trainer;
import java.util.List;

public interface TrainerService {
    Trainer createProfile(CreateTrainerProfileRequest request);

    Trainer findByUsername(UsernameRequest request);

    Trainer updatePassword(UpdatePasswordRequest request);

    Trainer updateTrainer(UpdateTrainerRequest request);

    Trainer setActiveStatus(UpdateUserActiveRequest request);

    List<Trainer> getTrainersWithoutTraineeByUsername(UsernameRequest request);
}
