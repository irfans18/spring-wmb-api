package com.enigma.wmb_api.constant.enums;

public enum TransactionType {
    DINE_IN("DI"),
    TAKE_AWAY("TA");
    public final String value;

    TransactionType(String value) {
        this.value = value;
    }
}
