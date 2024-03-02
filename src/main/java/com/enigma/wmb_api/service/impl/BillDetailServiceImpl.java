package com.enigma.wmb_api.service.impl;

import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Menu;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.BillDetailRequest;
import com.enigma.wmb_api.model.response.BillDetailResponse;
import com.enigma.wmb_api.repo.BillDetailRepo;
import com.enigma.wmb_api.service.BillDetailService;
import com.enigma.wmb_api.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class BillDetailServiceImpl implements BillDetailService {
    private final BillDetailRepo repo;
    private final MenuService menuService;

    @Override
    public BillDetailResponse create(BillDetailRequest request, Transaction trx) {
        Menu menu = menuService.findOrFail(request.getMenuId());
        BillDetail billDetail = BillDetail
                .builder()
                .menu(menu)
                .qty(request.getQty())
                .transaction(trx)
                .price(request.getQty() * menu.getPrice())
                .build();
        repo.saveAndFlush(billDetail);

        return BillDetailResponse.builder()
                .qty(billDetail.getQty())
                .price(billDetail.getPrice())
                .menuId(billDetail.getMenu().getId())
                .build();
    }

    @Override
    public List<BillDetail> createBatch(List<BillDetail> billDetails) {
        return repo.saveAllAndFlush(billDetails);
    }

    @Override
    public BillDetail findOrFail(String id) {
        return null;
    }

    @Override
    public List<BillDetail> findAll() {
        return null;
    }
}
