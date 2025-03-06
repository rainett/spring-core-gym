package com.rainett.model;

import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Trainee extends User {
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}
