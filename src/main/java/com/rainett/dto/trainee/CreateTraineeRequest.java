package com.rainett.dto.trainee;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateTraineeRequest {
    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String lastName;

    private LocalDate dateOfBirth;
    private String address;
}
