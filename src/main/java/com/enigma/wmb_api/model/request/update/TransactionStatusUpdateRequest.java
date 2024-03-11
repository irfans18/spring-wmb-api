package com.enigma.wmb_api.model.request.update;

import com.enigma.wmb_api.constant.enums.TransactionStatus;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionStatusUpdateRequest {
    private String orderId;
    private TransactionStatus transactionStatus;
}
