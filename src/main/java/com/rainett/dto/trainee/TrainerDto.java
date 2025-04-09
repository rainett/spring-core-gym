package com.rainett.dto.trainee;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerDto {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
}
