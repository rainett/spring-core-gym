package com.rainett.dto;

import lombok.Data;

@Data
public class AuthenticatedRequest {
    private String identity;
    private String password;
}
