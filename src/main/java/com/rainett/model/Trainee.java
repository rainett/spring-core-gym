package com.rainett.model;

import com.rainett.annotations.Id;
import java.time.LocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Trainee extends User {
    @Id
    private Long userId;
    private LocalDate dateOfBirth;
    private String address;
}
