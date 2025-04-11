package com.rainett.dto.trainee;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraineeResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String dateOfBirth;
    private String address;
    private Boolean isActive;
    private List<TrainerDto> trainers;
}
