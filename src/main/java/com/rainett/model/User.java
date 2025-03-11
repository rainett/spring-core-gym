package com.rainett.model;

import lombok.Data;
import lombok.ToString;

@Data
@ToString(exclude = "password")
public class User {
    private String firstName;
    private String lastName;
    private String username;
    private String password;
    private Boolean isActive;
}
