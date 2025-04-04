package com.rainett.dto.user;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserActiveRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    private boolean isActive;
}
