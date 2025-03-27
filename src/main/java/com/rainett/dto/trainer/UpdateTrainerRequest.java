package com.rainett.dto.trainer;

import com.rainett.dto.user.UpdateUserRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateTrainerRequest extends UpdateUserRequest {
    @NotBlank
    private String specialization;
}
