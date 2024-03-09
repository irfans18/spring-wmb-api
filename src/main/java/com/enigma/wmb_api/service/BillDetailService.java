package com.enigma.wmb_api.service;

import com.enigma.wmb_api.entity.BillDetail;
import com.enigma.wmb_api.entity.Transaction;
import com.enigma.wmb_api.model.request.BillDetailRequest;

import java.util.List;

public interface BillDetailService {

    BillDetail create(BillDetailRequest billDetail, Transaction trx);
    List<BillDetail> createBatch(List<BillDetail> billDetails);
    BillDetail findOrFail(String id);
    List<BillDetail> findAll();
}
