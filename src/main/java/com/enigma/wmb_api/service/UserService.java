package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.User;

public interface UserService {

    User create(User request);
    User update(User request);
    User findOrFail(String id);
    void delete(String id);

}
