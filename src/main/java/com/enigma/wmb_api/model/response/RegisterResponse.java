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
public class RegisterResponse {
    private String username;
    private Collection<String> roles;
}