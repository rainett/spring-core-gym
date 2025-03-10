package com.rainett.service;

import com.rainett.model.Trainee;

public interface TraineeService {
    Long createProfile(Trainee trainee);

    void updateProfile(Trainee trainee);

    void deleteProfile(Long userId);

    Trainee getProfile(Long userId);
}
