package com.enigma.wmb_api.constant.records;

import com.enigma.wmb_api.constant.enums.Color;

public record TestMessage(String message) {

    public String getMessage() {
        return Color.GREEN.value + message + Color.RESET.value;
    }
}
