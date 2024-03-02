package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, String>, JpaSpecificationExecutor<Transaction> {
}