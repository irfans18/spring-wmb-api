package com.enigma.wmb_api.service;


import com.enigma.wmb_api.entity.UserCredential;
import com.enigma.wmb_api.model.request.AuthRequest;
import com.enigma.wmb_api.model.response.LoginResponse;
import com.enigma.wmb_api.model.response.RegisterResponse;

public interface AuthService {
    RegisterResponse registerUser(AuthRequest request);
    RegisterResponse resgisterAdmin(AuthRequest request);
    LoginResponse login(AuthRequest request);
    UserCredential findOrFail(String id);
}
