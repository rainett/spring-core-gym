package com.rainett.dto.trainee;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTraineeTrainersRequest {
    @NotNull
    private List<String> trainersUsernames;
}
