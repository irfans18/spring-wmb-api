package com.enigma.wmb_api.model.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Menu}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class MenuRequest extends FilterRequest{
    private String id;
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Integer price;
    private Long minPrice;
    private Long maxPrice;
}