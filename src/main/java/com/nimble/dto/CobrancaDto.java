package com.nimble.dto;


import com.nimble.entity.MetodoPagamento;

import java.math.BigDecimal;

public record CobrancaDto(

        String cpfDestinatario,
        BigDecimal valor,
        String descricao,
    MetodoPagamento metodoPagamento
) {


}

