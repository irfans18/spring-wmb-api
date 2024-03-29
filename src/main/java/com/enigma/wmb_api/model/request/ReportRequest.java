package com.enigma.wmb_api.model.request;

import com.enigma.wmb_api.constant.enums.FileType;
import com.enigma.wmb_api.constant.enums.ReportPeriod;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportRequest {
    private ReportPeriod period;
    private FileType fileType;
    private boolean isSummarized;
}
