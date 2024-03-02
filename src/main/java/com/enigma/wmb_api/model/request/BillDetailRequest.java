package com.enigma.wmb_api.model.request;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * DTO for {@link com.enigma.wmb_api.entity.BillDetail}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillDetailRequest {
    private String menuId;
    private Integer qty;
}