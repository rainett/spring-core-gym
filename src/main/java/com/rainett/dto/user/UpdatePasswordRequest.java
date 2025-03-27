package com.rainett.dto.user;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePasswordRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    private String newPassword;
}
