package com.rainett.dto;

import lombok.Data;
import lombok.ToString;

@Data
public class AuthenticatedRequest {
    private String identity;
    @ToString.Exclude
    private String password;
}
