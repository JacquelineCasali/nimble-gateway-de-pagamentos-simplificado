package com.nimble.controller;

import com.nimble.dto.UserResponseDto;
import com.nimble.entity.User;

import com.nimble.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@Tag(name = "Usuário", description = "Informação do Usuário")
public class UserController {
    @Autowired
    private UserService userService;
    @PostMapping
    @Operation(summary = "Cadastro de usuário", description = "Essa função é responsável por cadastrar um usuário")

    public ResponseEntity<UserResponseDto> create(@Valid @RequestBody User user) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.createUser(user));
    }
    //todos os dados precisa de autenticação



}
