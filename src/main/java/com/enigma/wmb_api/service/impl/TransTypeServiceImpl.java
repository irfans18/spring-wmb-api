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
    public TransType getOrCreate(String transType) {
        return repo.findById(transType).orElseGet(
                () -> repo.save(TransType.builder()
                        .id(transType)
                        .description(TransactionType.valueOf(transType).name())
                        .build()
                )
        );
    }

}
