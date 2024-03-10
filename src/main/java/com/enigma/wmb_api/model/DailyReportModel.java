package com.enigma.wmb_api.model;

import com.enigma.wmb_api.constant.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class DailyReportModel {
    private String trxId;
    private String userId;
    private String description;
    private String tableName;
    private Date trxDate;
    private Integer total;
    private TransactionStatus status;
}
