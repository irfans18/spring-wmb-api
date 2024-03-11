package com.enigma.wmb_api.constant.enums;

import com.enigma.wmb_api.entity.TransType;

public enum TransactionType {
    DINE_IN("DI"),
    TAKE_AWAY("TA");
    public final String value;

    TransactionType(String value) {
        this.value = value;
    }

    public TransType getTransType() {
        return TransType.builder()
                .id(this.value)
                .description(this.name())
                .build();
    }
}
