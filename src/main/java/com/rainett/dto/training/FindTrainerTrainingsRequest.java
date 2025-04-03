package com.rainett.dto.training;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.NotBlank;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FindTrainerTrainingsRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    private LocalDate from;
    private LocalDate to;
    private String traineeUsername;
}