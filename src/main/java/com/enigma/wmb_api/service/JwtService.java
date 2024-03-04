package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.model.response.JwtClaims;

public interface JwtService {

    String generateToken(UserCredential credential);
    boolean verifyToken(String token);
    JwtClaims getClaimsFromToken(String token);
}
