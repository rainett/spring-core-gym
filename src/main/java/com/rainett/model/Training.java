package com.rainett.model;

import java.time.LocalDate;
import lombok.Data;

@Data
public class Training {
    private Long traineeId;
    private Long trainerId;
    private String name;
    private TrainingType trainingType;
    private LocalDate date;
    private Long duration;
}
