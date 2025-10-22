package com.nimble.controller;

import com.nimble.dto.CobrancaDto;
import com.nimble.entity.Cobranca;
import com.nimble.service.CobrancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/cobranca")

public class CobrancaController {

    @Autowired
    private CobrancaService cobrancaService;

    @PostMapping

    public ResponseEntity<Cobranca> create (@RequestBody CobrancaDto cobrancaDto) throws Exception {

        Cobranca  newTransaction= this.cobrancaService.criarCobranca(cobrancaDto);
        return new ResponseEntity<>(newTransaction, HttpStatus.OK);

    }
}

