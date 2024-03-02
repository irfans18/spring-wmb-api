package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.TransType;
import com.enigma.wmb_api.repo.TransTypeRepo;
import com.enigma.wmb_api.service.TransTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransTypeServiceImpl implements TransTypeService {
    private final TransTypeRepo repo;
    @Override
    public TransType create(TransType transType) {
        return repo.saveAndFlush(transType);
    }

    @Override
    public TransType findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TransType not found"));
    }

    @Override
    public List<TransType> getAll() {
        return repo.findAll();
    }


    @Override
    public void delete(String id) {
        TransType transType = findOrFail(id);
        repo.delete(transType);
    }
}
