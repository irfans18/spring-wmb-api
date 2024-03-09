package com.enigma.wmb_api.model.request.update;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link com.enigma.wmb_api.entity.DinningTable}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class DinningTableNewOrUpdateRequest {
    @NotBlank
    private String id;
    @NotBlank
    private String name;
}