package com.enigma.wmb_api.service;


import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;

public interface RoleService {
    Role getOrCreate(UserRole role);
    UserRole findOrFail(String id);
}
