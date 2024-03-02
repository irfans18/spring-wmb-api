package com.enigma.wmb_api.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * DTO for {@link com.enigma.wmb_api.entity.UserCredential}
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// @PasswordMatch(password = "password", passwordConfirmation = "passwordConfirmation")
public class AuthRequest {
    @Size(min = 4, max = 20)
    @NotBlank(message = "username is required")
    private String username;

    // @ValidPassword
    @NotBlank
    private String password;

    // private String passwordConfirmation;
}