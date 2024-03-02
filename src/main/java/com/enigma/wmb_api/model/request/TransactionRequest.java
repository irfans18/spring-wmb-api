package com.enigma.wmb_api.model.request;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Transaction}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class TransactionRequest {
    private String userId;
    private String dinningTableId;
    private List<BillDetailRequest> billDetails;
}