package com.rainett.service;

import com.rainett.model.Trainer;
import java.util.List;

public interface TrainerService {
    Trainer createProfile(Trainer trainer);

    Trainer updateProfile(Trainer trainer);

    Trainer getProfile(Long userId);

    List<Trainer> getAll();
}
