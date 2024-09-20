package com.pickpaysimplificado.controller;

import com.pickpaysimplificado.domain.user.User;
import com.pickpaysimplificado.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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


    public ResponseEntity create (@Valid @RequestBody User user){

       this.userService.createUser(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(user);

    }
    @GetMapping

    @Operation(summary = "Lista de usuários", description = "Essa função é responsável por listar os usuários")

    public ResponseEntity <List<User>> getAllUsers(){
        List<User> users= this.userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    }


