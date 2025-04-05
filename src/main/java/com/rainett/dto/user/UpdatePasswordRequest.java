package com.rainett.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdatePasswordRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(min = 6)
    @ToString.Exclude
    private String newPassword;
}
