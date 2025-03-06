package com.rainett.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class Trainer extends User {
    private Long userId;
    private String specialization;
}
