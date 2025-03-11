package com.rainett.dao;

import com.rainett.model.Training;
import java.util.List;

public interface TrainingDao {
    Training save(Training training);

    Training findById(Long id);

    List<Training> findAll();
}
