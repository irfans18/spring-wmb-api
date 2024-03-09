package com.enigma.wmb_api.model.response;

import com.enigma.wmb_api.model.request.FilterRequest;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link com.enigma.wmb_api.entity.DinningTable}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class DinningTableResponse {
    private String id;
    private String name;
}