package com.rainett.dao;

import com.rainett.model.Trainee;

public interface TraineeDao {
    Long save(Trainee trainee);

    Trainee findByUserId(Long userId);

    void deleteByUserId(Long userId);
}
