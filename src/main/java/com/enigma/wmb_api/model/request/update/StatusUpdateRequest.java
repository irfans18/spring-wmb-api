package com.enigma.wmb_api.model.request.update;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatusUpdateRequest {

    private String userId;
    private Boolean status;
}
