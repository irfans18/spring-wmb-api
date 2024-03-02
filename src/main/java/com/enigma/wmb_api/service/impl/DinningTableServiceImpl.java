package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.repo.DinningTableRepo;
import com.enigma.wmb_api.service.DinningTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DinningTableServiceImpl implements DinningTableService {
    private final DinningTableRepo repo;
    @Override
    public DinningTable findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dinning table not found"));
    }

    @Override
    public DinningTable findByNameOrFail(String name) {
        return repo.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dinning table not found"));
    }

    @Override
    public DinningTable create(DinningTable dinningTable) {
        return repo.saveAndFlush(dinningTable);
    }

    @Override
    public DinningTable update(DinningTable dinningTable) {
        findOrFail(dinningTable.getId());
        return repo.saveAndFlush(dinningTable);
    }

    @Override
    public List<DinningTable> findAll() {
        return repo.findAll();
    }

    @Override
    public void delete(String id) {
        findOrFail(id);
        repo.deleteById(id);
    }
}
