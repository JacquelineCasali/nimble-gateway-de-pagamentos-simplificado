package com.nimble.infra.exceptions;

import java.util.List;

public class MultiplasRegrasException extends RuntimeException {
    private final List<String> mensagens;

    public MultiplasRegrasException(List<String> mensagens) {
        this.mensagens = mensagens;
    }

    public List<String> getMensagens() {
        return mensagens;
    }
}
