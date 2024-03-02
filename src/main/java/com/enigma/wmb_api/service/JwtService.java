package com.enigma.wmb_api.service;

import com.enigma.wmb_api.model.response.JwtClaims;

public interface JwtService {

    String generateToken();
    boolean verifyToken(String token);
    JwtClaims getClaimsFromToken(String token);
}
