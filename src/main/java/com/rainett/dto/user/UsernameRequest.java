package com.rainett.dto.user;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UsernameRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;
}
