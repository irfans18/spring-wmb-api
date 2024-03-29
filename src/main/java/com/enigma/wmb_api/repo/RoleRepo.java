package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.constant.enums.UserRole;
import com.enigma.wmb_api.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepo extends JpaRepository<Role, String> {
    Optional<Role> findByRole(UserRole role);
}