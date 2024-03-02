package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.DinningTable;

import java.util.List;

public interface DinningTableService {
    DinningTable findOrFail(String id);
    DinningTable findByNameOrFail(String name);

    DinningTable create(DinningTable dinningTable);

    DinningTable update(DinningTable dinningTable);
    List<DinningTable> findAll();

    void delete(String id);


}
