package com.enigma.wmb_api.repo;

public interface TransactionRepo extends org.springframework.data.jpa.repository.JpaRepository<com.enigma.wmb_api.entity.Transaction, java.lang.String> ,org.springframework.data.jpa.repository.JpaSpecificationExecutor<com.enigma.wmb_api.entity.Transaction> {
}