package com.rainett.service;

import com.rainett.model.Trainer;

public interface TrainerService {
    Long createProfile(Trainer trainer);

    void updateProfile(Trainer trainer);

    Trainer getProfile(Long userId);
}
