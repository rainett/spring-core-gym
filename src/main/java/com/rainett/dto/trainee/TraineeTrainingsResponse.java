package com.rainett.dto.trainee;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TraineeTrainingsResponse {
    private String trainingName;
    private LocalDate date;
    private String trainingType;
    private long duration;
    private String trainerUsername;
}
