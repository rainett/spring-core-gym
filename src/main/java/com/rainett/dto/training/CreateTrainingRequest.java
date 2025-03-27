package com.rainett.dto.training;

import com.rainett.dto.AuthenticatedRequest;
import com.rainett.model.TrainingType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CreateTrainingRequest extends AuthenticatedRequest {
    @NotBlank
    private String traineeUsername;

    @NotBlank
    private String trainerUsername;

    @NotBlank
    private String name;

    @NotNull
    private TrainingType trainingType;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long duration;
}
