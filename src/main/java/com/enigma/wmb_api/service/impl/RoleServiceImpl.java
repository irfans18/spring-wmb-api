package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.entity.Role;
import com.enigma.wmb_api.repo.RoleRepo;
import com.enigma.wmb_api.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
}
