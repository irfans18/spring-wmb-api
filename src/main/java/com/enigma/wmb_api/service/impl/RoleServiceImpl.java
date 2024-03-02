package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repo.RoleRepo;
import com.enigma.wmb_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepo repo;
    @Override
    public Role getOrCreate(UserRole role) {
        return repo.findByRole(role).orElseGet(() -> repo.save(
                Role.builder().role(role).build()
        ));
    }

    @Override
    public UserRole findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Role not found")).getRole();
    }
}
