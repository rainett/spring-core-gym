package com.rainett.dto.trainer;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerTrainingResponse {
    private String trainingName;
    private String date;
    private String trainingType;
    private long duration;
    private String traineeUsername;
}
