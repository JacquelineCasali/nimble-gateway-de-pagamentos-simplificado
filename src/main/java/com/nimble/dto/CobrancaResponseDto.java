package com.nimble.dto;
import java.math.BigDecimal;
import java.time.LocalDateTime;
public record CobrancaResponseDto(
        Long id,
        UserResponseDto originador,
        UserResponseDto destinatario,
        BigDecimal valor,
        String descricao,
        com.nimble.entity.Status status,
        LocalDateTime dataCriacao
) {
}
