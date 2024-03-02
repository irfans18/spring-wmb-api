package com.enigma.wmb_api.model.response;

import com.enigma.wmb_api.model.request.AuthRequest;

public interface UserService {

    RegisterResponse createUser(AuthRequest request);
    LoginResponse login(AuthRequest request);
    User 

}
