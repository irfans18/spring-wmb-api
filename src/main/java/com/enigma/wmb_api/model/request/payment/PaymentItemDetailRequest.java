package com.enigma.wmb_api.model.request.payment;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentItemDetailRequest {
    private String id;
    private Integer price;
    private Integer quantity;
    private String name;
}

