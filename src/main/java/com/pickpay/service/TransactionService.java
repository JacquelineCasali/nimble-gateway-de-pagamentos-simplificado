package com.pickpay.service;

import com.pickpay.domain.transaction.Transaction;
import com.pickpay.domain.user.User;
import com.pickpay.dto.TransactionDTO;
import com.pickpay.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;
    @Autowired
    private TransactionRepository transactionRepository;

//    fazer comunicação entre serviços restTemplate
    @Autowired
   private RestTemplate restTemplate;

    @Autowired
    private  NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transaction) throws Exception {
// pegando o usuario
        User sender=this.userService.findUserById(transaction.senderId());
        User receiver=this.userService.findUserById(transaction.receiverId());

     // validando
        userService.validateTransaction(sender,transaction.value());

//verificando se não esta autorizado
//        boolean isAuthorized=this.authorizeTransaction(sender,transaction.value());
//        if(!isAuthorized){
//throw  new Exception("Transação não autorizada");
//        }

        Transaction newTransaction= new Transaction();
        newTransaction.setAmount(transaction.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());
// atualizar saldo dos usuarios e salvar a transação

// atualizar valores saldo menos o valor da transação
        sender.setBalance(sender.getBalance().subtract(transaction.value()));
        receiver.setBalance(receiver.getBalance().add(transaction.value()));

       this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);


        this.notificationService.sendNotification(sender,"Transação realizada com sucesso");
        this.notificationService.sendNotification(receiver,"Transação recebida com sucesso");
        System.out.println("Transação realizada com sucesso");
        return newTransaction;

    }
    // Antes de finalizar a transferência, deve-se consultar um serviço autorizador externo
//    public  boolean authorizeTransaction(User sender, BigDecimal valeu){
//ResponseEntity<Map> authorizationResponse= restTemplate.getForEntity("https://run.mocky.io/v3/8fafdd68-a090-496f-8c9a-3442cf30dae6", Map.class);
//if(authorizationResponse.getStatusCode() == HttpStatus.OK ){
//    String message=(String) authorizationResponse.getBody().get("message");
//
//    return "Autozirado".equalsIgnoreCase(message);
//
//}else return  false;
//
//    }
}
