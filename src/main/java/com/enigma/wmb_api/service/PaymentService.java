package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Payment;
import com.enigma.wmb_api.entity.Transaction;

public interface PaymentService {
    Payment create(Transaction transaction);

    // void checkFailedAndUpdatePayments();
}
