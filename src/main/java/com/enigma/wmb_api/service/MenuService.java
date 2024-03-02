package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.response.MenuResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface MenuService {
    Menu create(Menu request);
    MenuResponse create(MenuRequest request);

    Menu findOrFail(String id);
    Menu update(Menu request);
    MenuResponse update(MenuRequest request);

    void delete(String id);
    List<Menu> findAll();
    Page<Menu> findAll(MenuRequest request);
}
