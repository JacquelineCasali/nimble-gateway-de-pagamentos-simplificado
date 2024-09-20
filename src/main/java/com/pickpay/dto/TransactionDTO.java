package com.pickpay.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

public record TransactionDTO(

        BigDecimal value,
        Long senderId,
        Long receiverId){
}
