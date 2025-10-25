package com.nimble.controller;

import com.nimble.dto.CobrancaDto;
import com.nimble.dto.CobrancaResponseDto;
import com.nimble.dto.PagamentoCartaoDto;
import com.nimble.entity.Status;
import com.nimble.service.CobrancaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.List;
import com.nimble.entity.User;

@RestController
@RequestMapping("/cobranca")
@Tag(name = "Cobrança", description = "Gerenciamento de cobrança e pagamento")
//jwt exigido para todas as rotas
@SecurityRequirement(name="bearer-key")
public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping
    @Operation(summary = "Criar cobrança", description = "Criar cobrança para o usuário")

    public ResponseEntity<CobrancaResponseDto> create (@RequestBody CobrancaDto cobrancaDto) throws Exception {
        // Recupera o usuário autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();

        var  newCobranca= this.cobrancaService.criarCobranca(cobrancaDto,originador);
        return ResponseEntity.ok(newCobranca);

    }
    @GetMapping("/enviadas")
    @Operation(summary = "Listar cobrança enviadas", description = "Listar todas as cobrança enviadas")

    public List<CobrancaResponseDto> listarEnviadas(@RequestParam Status status) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();

        return cobrancaService.listarCobrancasEnviadas(originador.getCpf(), status);
    }

    @GetMapping("/recebidas")
    @Operation(summary = "Listar cobrança recebidas", description = "Listar todas as cobrança recebidas")

    public List<CobrancaResponseDto> listarrecebidos(@RequestParam Status status) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User destinatario = (User) authentication.getPrincipal();
        return cobrancaService.listarCobrancasRecebidas(destinatario.getCpf(),status);
    }

    @PostMapping("/pagar")
    @Operation(summary = "Pagar cobrança", description = "Efetuar o pagamento ")

    public ResponseEntity<CobrancaResponseDto> pagar(
            @RequestBody PagamentoCartaoDto dto,
            @AuthenticationPrincipal User user
    ) throws Exception {
        return ResponseEntity.ok(cobrancaService.pagarCobranca(dto, user));
    }

    @PutMapping("/cancelar/{id}")
    @Operation(summary = "Cancelar cobrança ", description = "Efetuar o cancelamento de cobrança enviadas")

    public ResponseEntity<CobrancaResponseDto> cancelarCobranca(@PathVariable Long id) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User originador = (User) authentication.getPrincipal();
        var cobrancaCancelada = cobrancaService.cancelarCobranca(id,originador);
        return ResponseEntity.ok(cobrancaCancelada);
    }







}