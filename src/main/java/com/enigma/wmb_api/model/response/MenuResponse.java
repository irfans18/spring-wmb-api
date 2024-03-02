package com.enigma.wmb_api.model.response;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * DTO for {@link com.enigma.wmb_api.entity.Menu}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MenuResponse {
    @NotBlank
    private String name;
    @NotNull
    @Min(0)
    private Integer price;
}