package com.enigma.wmb_api.model.request.payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailRequest {
    @JsonProperty("order_id")
    private String orderId;
    @JsonProperty("gross_amount")
    private Integer amount;
}
