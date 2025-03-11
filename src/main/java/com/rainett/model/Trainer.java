package com.rainett.model;

import com.rainett.annotations.Id;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Trainer extends User {
    @Id
    private Long userId;
    private String specialization;
}
