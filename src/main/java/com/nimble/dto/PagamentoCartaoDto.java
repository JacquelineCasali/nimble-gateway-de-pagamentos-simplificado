package com.nimble.dto;

import java.math.BigDecimal;

public record PagamentoCartaoDto(
        Long cobrancaId,
        BigDecimal valor,
        String numeroCartao,
        String nomeTitular,
        String dataExpiracao,
        String cvv
) {
}
