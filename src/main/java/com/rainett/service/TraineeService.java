package com.rainett.service;

import com.rainett.model.Trainee;
import java.util.List;

public interface TraineeService {
    Trainee createProfile(Trainee trainee);

    Trainee updateProfile(Trainee trainee);

    void deleteProfile(Long userId);

    Trainee getProfile(Long userId);

    List<Trainee> getAll();
}
