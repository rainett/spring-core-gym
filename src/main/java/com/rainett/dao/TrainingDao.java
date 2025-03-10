package com.rainett.dao;

import com.rainett.model.Training;

public interface TrainingDao {
    Long save(Training training);

    Training findById(Long id);
}
