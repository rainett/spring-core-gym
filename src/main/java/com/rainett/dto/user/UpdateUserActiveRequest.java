package com.rainett.dto.user;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserActiveRequest {
    @NotNull
    private Boolean isActive;
}
