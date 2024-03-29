package com.enigma.wmb_api.model.response;

import lombok.*;

import java.util.Collection;
import java.util.Date;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Transaction}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionResponse {
    private String id;
    private Date trxDate;
    private String userId;
    private String dinningTableId;
    private String transType;
    private Collection<BillDetailResponse> billDetails;
    private PaymentResponse payment;

}