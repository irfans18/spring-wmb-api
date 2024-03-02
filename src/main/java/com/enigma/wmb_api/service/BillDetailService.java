package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.BillDetailRequest;
import com.enigma.wmb_api.model.response.BillDetailResponse;

import java.util.List;

public interface BillDetailService {

    BillDetailResponse create(BillDetailRequest billDetail, Transaction trx);
    List<BillDetail> createBatch(List<BillDetail> billDetails);
    BillDetail findOrFail(String id);
    List<BillDetail> findAll();
}
