package com.rainett.service;

import com.rainett.model.Trainee;

public interface TraineeService {
    void createProfile(Trainee trainee);

    void updateProfile(Trainee trainee);

    void deleteProfile(Long userId);

    Trainee getProfile(Long userId);
}
