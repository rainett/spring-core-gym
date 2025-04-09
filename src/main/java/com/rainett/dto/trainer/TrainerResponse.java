package com.rainett.dto.trainer;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainerResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private Boolean isActive;
    private List<TraineeDto> trainees;
}
