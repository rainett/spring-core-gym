package com.rainett.dto.training;

import com.rainett.dto.AuthenticatedRequest;
import com.rainett.model.TrainingType;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FindTraineeTrainingsRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    private LocalDate from;
    private LocalDate to;
    private String trainerUsername;
    private TrainingType trainingType;
}
