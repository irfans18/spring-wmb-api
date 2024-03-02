package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.DinningTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DinningTableRepo extends JpaRepository<DinningTable, String> {
    Optional<DinningTable> findByName(String name);
}