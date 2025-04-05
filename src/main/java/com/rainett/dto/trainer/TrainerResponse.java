package com.rainett.dto.trainer;

import java.util.List;
import lombok.Data;

@Data
public class TrainerResponse {
    private String username;
    private String firstName;
    private String lastName;
    private String specialization;
    private boolean isActive;
    private List<TraineeDto> trainees;

    @Data
    private static class TraineeDto {
        private String username;
        private String firstName;
        private String lastName;
    }
}
