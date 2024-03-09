package com.enigma.wmb_api.model.response;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Menu}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MenuResponse {
    private String id;
    private String name;
    private Integer price;
}