package com.rainett.dto.training;

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

    @NotBlank
    private String trainingType;

    @NotNull
    private LocalDate date;

    @NotNull
    private Long duration;
}
