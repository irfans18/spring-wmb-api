package com.enigma.wmb_api.model.response;

import com.enigma.wmb_api.entity.BillDetail;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * DTO for {@link BillDetail}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BillDetailResponse {
    private String id;
    private String menuId;
    private Integer qty;
    private Integer price;
}