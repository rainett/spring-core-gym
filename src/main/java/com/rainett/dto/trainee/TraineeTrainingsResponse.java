package com.rainett.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeTrainingsResponse {
    private String trainingName;
    private String date;
    private String trainingType;
    private long duration;
    private String trainerUsername;
}
