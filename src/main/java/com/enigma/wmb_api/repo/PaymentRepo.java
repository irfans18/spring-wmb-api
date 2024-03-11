package com.enigma.wmb_api.repo;

import com.enigma.wmb_api.constant.enums.TransactionStatus;
import com.enigma.wmb_api.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface PaymentRepo extends JpaRepository<Payment, String>, JpaSpecificationExecutor<Payment> {
    List<Payment> findAllByTransactionStatusIn(List<TransactionStatus> statusList);
}