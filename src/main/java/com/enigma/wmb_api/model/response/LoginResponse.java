package com.enigma.wmb_api.model.response;

import lombok.*;

import java.util.Collection;

/**
 * DTO for {@link com.enigma.enigma_shop.entity.UserCredential}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoginResponse {
    private String username;
    private String token;
    private Collection<String> roles;
}