package com.enigma.wmb_api.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link com.enigma.wmb_api.entity.DinningTable}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DinningTableRequest extends FilterRequest {
    private String id;
    private String name;
}