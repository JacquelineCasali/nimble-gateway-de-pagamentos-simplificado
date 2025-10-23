package com.nimble.controller;

import com.nimble.dto.CobrancaDto;
import com.nimble.dto.CobrancaResponseDto;
import com.nimble.entity.Status;
import com.nimble.service.CobrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import com.nimble.entity.User;

@RestController
@RequestMapping("/cobranca")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping

    public ResponseEntity<CobrancaResponseDto> create (@RequestBody CobrancaDto cobrancaDto) throws Exception {
        // Recupera o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();

        var  newCobranca= this.cobrancaService.criarCobranca(cobrancaDto,originador);
        return ResponseEntity.ok(newCobranca);

    }
    @GetMapping("/enviadas")
    public List<CobrancaResponseDto> listarEnviadas(@RequestParam Status status) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();

        return cobrancaService.listarCobrancasEnviadas(originador.getCpf(), status);
    }

    @GetMapping("/recebidas")
    public List<CobrancaResponseDto> listarrecebidos(@RequestParam Status status) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User destinatario = (User) authentication.getPrincipal();
        return cobrancaService.listarCobrancasRecebidas(destinatario.getCpf(),status);
    }
    @PutMapping("/pagar/{id}")
    public ResponseEntity<CobrancaResponseDto> pagarCobranca(@PathVariable Long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();
        var cobrancaPaga = cobrancaService.pagarCobranca(id, originador);
        return ResponseEntity.ok(cobrancaPaga);
    }

    @PutMapping("/cancelar/{id}")
    public ResponseEntity<CobrancaResponseDto> cancelarCobranca(@PathVariable Long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();
        var cobrancaCancelada = cobrancaService.cancelarCobranca(id,originador);
        return ResponseEntity.ok(cobrancaCancelada);
    }

//pagar com o cartao
//    @PutMapping("/pagarCartao/{id}")
//    public ResponseEntity<CobrancaResponseDto> pagarPorCartao(@PathVariable Long id, @RequestBody PagamentoCartaoDto pagamentoDto) throws Exception {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        User pagador = (User) authentication.getPrincipal();
//        var cobrancaPaga = cobrancaService.pagarPorCartao(id, pagador, pagamentoDto);
//        return ResponseEntity.ok(cobrancaPaga);
//    }

}