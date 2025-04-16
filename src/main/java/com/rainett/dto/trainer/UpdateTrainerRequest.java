package com.rainett.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateTrainerRequest {
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String lastName;

    @NotNull
    private Boolean isActive;

    @NotBlank
    private String specialization;
}
