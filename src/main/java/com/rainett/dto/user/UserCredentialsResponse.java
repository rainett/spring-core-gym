package com.rainett.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserCredentialsResponse {
    private String username;
    private String password;
    private String token;
}
