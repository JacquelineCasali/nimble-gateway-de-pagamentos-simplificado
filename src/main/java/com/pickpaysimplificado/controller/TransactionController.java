package com.pickpaysimplificado.controller;

import com.pickpaysimplificado.domain.transaction.Transaction;

import com.pickpaysimplificado.dto.TransactionDTO;
import com.pickpaysimplificado.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
@Tag(name = "Transferência ")

public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Realizar Transferência", description = "Essa função é responsável por realizar transfêrencia")

    public ResponseEntity<Transaction> create (@RequestBody TransactionDTO transaction) throws Exception {

       Transaction  newTransaction= this.transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction,HttpStatus.OK);

    }

}
