package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import com.enigma.wmb_api.model.request.update.DinningTableNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.DinningTableResponse;
import org.springframework.data.domain.Page;

public interface DinningTableService {
    DinningTable findOrFail(String id);

    DinningTableResponse create(DinningTableNewOrUpdateRequest dinningTable);

    DinningTableResponse update(DinningTableNewOrUpdateRequest dinningTable);
    Page<DinningTableResponse> findAll(DinningTableRequest request);

    void delete(String id);


}
