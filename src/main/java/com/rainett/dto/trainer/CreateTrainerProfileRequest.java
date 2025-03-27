package com.rainett.dto.trainer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateTrainerProfileRequest {
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String lastName;

    @NotBlank
    private String specialization;
}
