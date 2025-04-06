package com.rainett.dto.trainer;

import java.time.LocalDate;
import lombok.Data;

@Data
public class TrainerTrainingResponse {
    private String trainingName;
    private LocalDate date;
    private String trainingType;
    private long duration;
    private String traineeUsername;
}
