package com.rainett.dto.user;

import java.time.LocalDate;
import lombok.Data;

@Data
public class UserTrainingsResponse {
    private String trainingName;
    private LocalDate date;
    private String trainingType;
    private long duration;
    private String partnerUsername;
}
