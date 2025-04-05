package com.rainett.dto.trainee;

import java.time.LocalDate;
import java.util.List;
import lombok.Data;

@Data
public class TraineeResponse {
    private String username;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String address;
    private boolean isActive;
    private List<TraineeTrainerDto> trainers;
}
