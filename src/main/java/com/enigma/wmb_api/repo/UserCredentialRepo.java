package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.UserCredential;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserCredentialRepo extends JpaRepository<UserCredential, String> {
    Optional<UserCredential> findFirstByUsername(String username);
}
