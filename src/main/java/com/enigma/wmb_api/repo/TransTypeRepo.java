package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.TransType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransTypeRepo extends JpaRepository<TransType, String> {

}
