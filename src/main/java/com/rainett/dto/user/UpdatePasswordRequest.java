package com.rainett.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePasswordRequest {
    @NotBlank
    @ToString.Exclude
    private String oldPassword;

    @Size(min = 6)
    @ToString.Exclude
    private String newPassword;
}
