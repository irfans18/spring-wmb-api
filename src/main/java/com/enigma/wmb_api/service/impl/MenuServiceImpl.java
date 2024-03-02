package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.response.MenuResponse;
import com.enigma.wmb_api.repo.MenuRepo;
import com.enigma.wmb_api.service.MenuService;
import com.enigma.wmb_api.specification.MenuSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepo repo;
    @Override
    public Menu create(Menu request) {
        return repo.saveAndFlush(request);
    }

    @Override
    public MenuResponse create(MenuRequest request) {
        Menu menu = Menu.builder()
                .name(request.getName())
                .price(request.getPrice())
                .build();
        repo.saveAndFlush(menu);

        return MenuResponse.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }

    @Override
    public Menu findOrFail(String id) {
        return repo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Menu not found"));
    }

    @Override
    public Menu update(Menu request) {
        findOrFail(request.getId());
        return repo.saveAndFlush(request);
    }

    @Override
    public MenuResponse update(MenuRequest request) {
        Menu menu = findOrFail(request.getId());
        menu.setName(request.getName());
        menu.setPrice(request.getPrice());

        repo.saveAndFlush(menu);

        return MenuResponse.builder()
                .name(menu.getName())
                .price(menu.getPrice())
                .build();
    }

    @Override
    public void delete(String id) {
        Menu menu = findOrFail(id);
        repo.delete(menu);
    }

    @Override
    public List<Menu> findAll() {
        return repo.findAll();
    }

    @Override
    public Page<Menu> findAll(MenuRequest request) {
        if (request.getPage() <= 0) request.setPage(1);
        Sort sortBy = Sort.by(Sort.Direction.fromString(request.getDirection()), request.getSortBy());
        Pageable pageRequest = PageRequest.of(request.getPage()-1, request.getSize(), sortBy);
        Specification<Menu> specification = MenuSpecification.getSpecification(request);
        return repo.findAll(specification, pageRequest);
    }
}
