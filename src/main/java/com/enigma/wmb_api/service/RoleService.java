package com.enigma.wmb_api.service;


import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.entity.Role;

public interface RoleService {
    Role getOrCreate(UserRole role);
}
