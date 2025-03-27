package com.rainett.dto.user;

import com.rainett.dto.AuthenticatedRequest;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateUserRequest extends AuthenticatedRequest {
    @NotBlank
    private String username;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String firstName;

    @NotBlank
    @Size(max = 50)
    @Pattern(regexp = "^[A-Za-z]+(?:[\\s'-][A-Za-z]+)*$")
    private String lastName;
}
