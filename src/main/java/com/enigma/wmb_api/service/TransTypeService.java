package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.TransType;

import java.util.List;

public interface TransTypeService {
    TransType create(TransType transType);
    TransType findByIdOrThrowError(String id);
    List<TransType> getAll();
    TransType update(TransType transType);
    void delete(String id);
}
