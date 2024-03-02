package com.enigma.wmb_api.model.response;

import lombok.*;

/**
 * DTO for {@link com.enigma.wmb_api.Paging}
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagingResponse {
    private Integer totalPages;
    private Long totalElement;
    private Integer page;
    private Integer size;
    private Boolean hasNext;
    private Boolean hasPrevious;
}
