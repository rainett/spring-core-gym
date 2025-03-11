package com.rainett.service;

import com.rainett.model.Training;
import java.util.List;

public interface TrainingService {
    Training createTraining(Training training);

    Training getTraining(Long id);

    List<Training> getAll();
}
