package com.nimble.dto;
import java.math.BigDecimal;
public record UserResponseDto(
        Long id,
        String nome,
        String cpf,
        String email,
        BigDecimal saldo


) {
}
