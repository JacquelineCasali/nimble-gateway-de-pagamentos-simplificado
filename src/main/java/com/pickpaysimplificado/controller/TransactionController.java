package com.pickpaysimplificado.controller;

import com.pickpaysimplificado.domain.transaction.Transaction;
import com.pickpaysimplificado.domain.user.User;
import com.pickpaysimplificado.dto.TransactionDTO;
import com.pickpaysimplificado.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transfer")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> create (@RequestBody TransactionDTO transaction) throws Exception {

       Transaction  newTransaction= this.transactionService.createTransaction(transaction);
        return new ResponseEntity<>(newTransaction,HttpStatus.OK);
//        try{
//            this.transactionService.createTransaction(transaction);
//            return ResponseEntity.status(HttpStatus.CREATED).body(transaction);
//        }catch (Exception e){
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        }
    }

}
