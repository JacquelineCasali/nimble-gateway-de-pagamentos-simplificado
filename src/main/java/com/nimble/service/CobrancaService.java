package com.nimble.service;


import com.nimble.dto.CobrancaDto;
import com.nimble.entity.Cobranca;
import com.nimble.entity.User;
import com.nimble.repository.CobrancaRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class CobrancaService {

    @Autowired
    private UserService userService;
    @Autowired
    private CobrancaRepository cobrancaRepository;

    //    fazer comunicação entre serviços restTemplate
    @Autowired
    private RestTemplate restTemplate;



    public Cobranca criarCobranca(CobrancaDto cobrancaDto) throws Exception {
// pegando o usuario
        // Buscar usuários pelo CPF
        User originador = userService.findByCpf(cobrancaDto.cpfOriginador());
        User destinatario = userService.findByCpf(cobrancaDto.cpfDestinatario());

        // validando
        userService.validateCobranca(originador,cobrancaDto.valor());

//verificando se não esta autorizado
//        boolean isAuthorized=this.authorizeTransaction(sender,transaction.value());
//        if(!isAuthorized){
//throw  new Exception("Transação não autorizada");
//        }

        Cobranca newCobranca= new Cobranca();
        newCobranca.setValor(cobrancaDto.valor());
        newCobranca.setDescricao(cobrancaDto.descricao());
        newCobranca.setOriginador(originador);
        newCobranca.setDestinatario(destinatario);
        newCobranca.setDataCriacao(LocalDateTime.now());
// atualizar saldo dos usuarios e salvar a transação

// atualizar valores saldo menos o valor da transação
        originador.setSaldo(originador.getSaldo().subtract(cobrancaDto.valor()));
        destinatario.setSaldo(destinatario.getSaldo().add(cobrancaDto.valor()));

        this.cobrancaRepository.save(newCobranca);
        this.userService.saveUser(originador);
        this.userService.saveUser(destinatario);


//        this.notificationService.sendNotification(sender,"Transação realizada com sucesso");
//        this.notificationService.sendNotification(receiver,"Transação recebida com sucesso");
        System.out.println("Transação realizada com sucesso");
        return newCobranca;

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
