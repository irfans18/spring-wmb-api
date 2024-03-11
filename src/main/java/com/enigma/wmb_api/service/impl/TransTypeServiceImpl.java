package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.constant.enums.TransactionType;
import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repo.TransTypeRepo;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepo repo;

    @Override
    public TransType getOrCreate(TransactionType transType) {
        return repo.findById(transType.value).orElseGet(
                () -> repo.saveAndFlush(transType.getTransType()));
    }
}
