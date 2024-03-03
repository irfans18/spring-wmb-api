package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.User;
import com.enigma.wmb_api.model.request.UpdateStatusRequest;
import com.enigma.wmb_api.model.request.UserRequest;
import com.enigma.wmb_api.model.response.UserResponse;
import org.springframework.data.domain.Page;

public interface UserService {

    User create(User request);
    User update(User request);
    UserResponse updateStatusById(UpdateStatusRequest request);
    UserResponse update(UserRequest request);
    User findOrFail(String id);
    void delete(String id);
    Page<UserResponse> findAll(UserRequest request);
}
