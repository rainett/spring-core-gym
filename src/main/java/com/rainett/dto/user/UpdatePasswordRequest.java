package com.rainett.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
public class UpdatePasswordRequest {
    @NotBlank
    @ToString.Exclude
    private String oldPassword;

    @NotBlank
    @Size(min = 6)
    @ToString.Exclude
    private String newPassword;
}
