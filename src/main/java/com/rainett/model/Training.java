package com.rainett.model;

import com.rainett.annotations.Id;
import java.time.LocalDate;
import lombok.Data;

@Data
public class Training {
    @Id
    private Long id;
    private Long traineeId;
    private Long trainerId;
    private String name;
    private TrainingType trainingType;
    private LocalDate date;
    private Long duration;
}
