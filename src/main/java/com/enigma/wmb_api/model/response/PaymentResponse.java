package com.enigma.wmb_api.model.response;

import com.enigma.wmb_api.constant.TransactionStatus;
import lombok.*;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Payment}
 */
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentResponse {
    private String id;
    private String token;
    private String redirectUrl;
    private TransactionStatus transactionStatus;
}