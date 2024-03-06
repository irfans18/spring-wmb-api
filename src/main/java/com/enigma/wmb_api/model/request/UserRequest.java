package com.enigma.wmb_api.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;

/**
 * DTO for {@link com.enigma.wmb_api.entity.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class UserRequest extends FilterRequest {
    private String id;
    private String name;
    private String phoneNumber;
    private Boolean status;
}