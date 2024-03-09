package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.request.update.MenuNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.MenuResponse;
import com.enigma.wmb_api.repo.MenuRepo;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.MenuSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepo repo;
    @Override
    public MenuResponse create(MenuNewOrUpdateRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        return mapToResponse(repo.saveAndFlush(menu));
    }


    @Override
    public Menu findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
    }

    @Override
    public MenuResponse update(MenuNewOrUpdateRequest request) {
        Menu menu = findOrFail(request.getId());
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());
        return mapToResponse(repo.saveAndFlush(menu));
    }


    @Override
    public void delete(String id) {
        Menu menu = findOrFail(id);
        repo.delete(menu);
    }


    @Override
    public Page<MenuResponse> findAll(MenuRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize(), sortBy);
        Specification<Menu> specification = MenuSpecification.getSpecification(request);
        Page<Menu> pages = repo.findAll(specification, pageRequest);
        return new PageImpl<>(
                pages.getContent().stream().map(menu -> MenuResponse
                        .builder()
                        .id(menu.getId())
                        .name(menu.getName())
                        .price(menu.getPrice())
                        .build()
                ).toList(),
                pages.getPageable(),
                pages.getTotalElements()
        );
    }

    private MenuResponse mapToResponse(Menu menu) {
        return MenuResponse.builder()
                .id(menu.getId())
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }
}
