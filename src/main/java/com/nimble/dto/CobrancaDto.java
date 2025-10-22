package com.nimble.dto;


import java.math.BigDecimal;

public record CobrancaDto(

        String cpfOriginador,
        String cpfDestinatario,
        BigDecimal valor,
        String descricao

) {

}

