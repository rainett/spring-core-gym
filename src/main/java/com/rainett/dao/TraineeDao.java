package com.rainett.dao;

import com.rainett.model.Trainee;

public interface TraineeDao {
    void save(Trainee trainee);

    Trainee findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
