package com.enigma.wmb_api.service;

import com.enigma.wmb_api.constant.enums.TransactionType;
import com.enigma.wmb_api.entity.TransType;

public interface TransTypeService {
    TransType getOrCreate(TransactionType transType);

}
