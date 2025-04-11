package com.rainett.dto.trainee;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;

@Data
public class UpdateTraineeTrainersRequest {
    @NotNull
    private List<String> trainersUsernames;
}
