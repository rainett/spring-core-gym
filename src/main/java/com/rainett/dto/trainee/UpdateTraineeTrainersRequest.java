package com.rainett.dto.trainee;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateTraineeTrainersRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    @NotNull
    private List<String> trainersUsernames;
}
