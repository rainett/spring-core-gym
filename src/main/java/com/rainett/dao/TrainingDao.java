package com.rainett.dao;

import com.rainett.model.Training;

public interface TrainingDao {
    void save(Training training);

    Training findById(Long id);
}
