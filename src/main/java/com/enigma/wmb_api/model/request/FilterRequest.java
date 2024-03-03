package com.enigma.wmb_api.model.request;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class FilterRequest {
    private Integer page;
    private Integer size;
    private String sortBy;
    private String direction;
}
