package com.nimble.entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum MetodoPagamento {
    SALDO, CARTAO;

    @JsonCreator
    public static Status from(String value) {
        return Status.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();

    }
}
