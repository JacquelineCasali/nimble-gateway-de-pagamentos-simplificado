package com.nimble.infra.config;

import com.nimble.entity.Status;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToStatusConverter implements Converter<String, Status> {
    @Override
    public Status convert(String source) {
        if (source == null) return null;
        try {
            return Status.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Status inválido: " + source +
                    ". Use: PENDENTE, PAGA ou CANCELADA");
        }
    }
}
