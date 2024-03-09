package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.DinningTable;
import com.enigma.wmb_api.model.request.DinningTableRequest;
import com.enigma.wmb_api.model.request.update.DinningTableNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.DinningTableResponse;
import com.enigma.wmb_api.repo.DinningTableRepo;
import com.enigma.wmb_api.service.DinningTableService;
import com.enigma.wmb_api.specification.DinningTableSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class DinningTableServiceImpl implements DinningTableService {
    private final DinningTableRepo repo;
    @Override
    public DinningTable findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Dinning table not found"));
    }

    @Override
    public DinningTableResponse create(DinningTableNewOrUpdateRequest request) {
        DinningTable dinningTable = DinningTable.builder()
                .name(request.getName())
                .build();
        return mapToResponse(repo.saveAndFlush(dinningTable));
    }

    @Override
    public DinningTableResponse update(DinningTableNewOrUpdateRequest request) {
        DinningTable dinningTable = findOrFail(request.getId());
        dinningTable.setName(request.getName());
        return mapToResponse(repo.saveAndFlush(dinningTable));
    }

    @Override
    public Page<DinningTableResponse> findAll(DinningTableRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize(), sortBy);
        Specification<DinningTable> specification = DinningTableSpecification.getSpecification(request);

        Page<DinningTable> pages = repo.findAll(specification, pageRequest);
        return new PageImpl<>(
                pages.getContent().stream().map(dinningTable -> DinningTableResponse
                        .builder()
                        .id(dinningTable.getId())
                        .name(dinningTable.getName())
                        .build()
                ).toList(),
                pages.getPageable(),
                pages.getTotalElements()
        );
    }

    @Override
    public void delete(String id) {
        findOrFail(id);
        repo.deleteById(id);
    }

    private DinningTableResponse mapToResponse(DinningTable dinningTable) {
        return DinningTableResponse.builder()
                .id(dinningTable.getId())
                .name(dinningTable.getName())
                .build();
    }
}
