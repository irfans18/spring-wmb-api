package com.enigma.wmb_api.model.request;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateStatusRequest {

    private String userId;
    private Boolean status;
}
