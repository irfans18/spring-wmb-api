package com.enigma.wmb_api.constant.enums;

public enum Color {
    RESET  ("\u001B[0m"),
    RED  ("\u001B[31m"),
    GREEN  ("\u001B[32m"),
    YELLOW  ("\u001B[33m");
    public String value;

    Color(String value) {
        this.value = value;
    }

}
