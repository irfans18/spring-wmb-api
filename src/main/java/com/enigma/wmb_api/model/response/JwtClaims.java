package com.enigma.wmb_api.model.response;

import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtClaims {
    private String userCredentialId;
    private List<String> roles;
}
