package com.enigma.wmb_api.model.response;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * DTO for {@link com.enigma.wmb_api.entity.User}
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UserResponse {
    private String id;
    private String name;
    private String phoneNumber;
    private Boolean status;
}