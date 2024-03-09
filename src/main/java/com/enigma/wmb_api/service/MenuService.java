package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.model.request.MenuRequest;
import com.enigma.wmb_api.model.request.update.MenuNewOrUpdateRequest;
import com.enigma.wmb_api.model.response.MenuResponse;
import org.springframework.data.domain.Page;

public interface MenuService {
    MenuResponse create(MenuNewOrUpdateRequest request);
    Menu findOrFail(String id);
    MenuResponse update(MenuNewOrUpdateRequest request);

    void delete(String id);
    Page<MenuResponse> findAll(MenuRequest request);
}
