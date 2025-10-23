package com.nimble.dto;
import com.nimble.entity.Status;


import java.math.BigDecimal;
import java.time.LocalDateTime;


public record CobrancaResponseDto(
        Long id,
        UserResponseDto originador,
        UserResponseDto destinatario,
        BigDecimal valor,
        String descricao,
        Status status,
        LocalDateTime dataCriacao
) {


}
