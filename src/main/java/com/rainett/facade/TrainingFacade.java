package com.rainett.facade;

import com.rainett.service.TraineeService;
import com.rainett.service.TrainerService;
import com.rainett.service.TrainingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TrainingFacade {
    private final TrainingService trainingService;
    private final TraineeService traineeService;
    private final TrainerService trainerService;
}
