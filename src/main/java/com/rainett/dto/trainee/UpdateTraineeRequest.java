package com.rainett.dto.trainee;

import com.rainett.dto.user.UpdateUserRequest;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateTraineeRequest extends UpdateUserRequest {
    private LocalDate dateOfBirth;
    private String address;
}
