package com.nimble.dto;


import java.math.BigDecimal;

public record CobrancaDto(

        String cpfDestinatario,
        BigDecimal valor,
        String descricao

) {


}

