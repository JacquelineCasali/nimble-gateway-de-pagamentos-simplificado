package com.nimble.entity;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
public enum MetodoPagamento {
    SALDO, CARTAO,AGUARDANDO_PAGAMENTO;

    @JsonCreator
    public static MetodoPagamento from(String value) {
        return MetodoPagamento.valueOf(value.toUpperCase());
    }

    @JsonValue
    public String toValue() {
        return this.name();

    }
}
