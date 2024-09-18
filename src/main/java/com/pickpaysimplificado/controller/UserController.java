package com.pickpaysimplificado.controller;

import com.pickpaysimplificado.domain.user.User;
import com.pickpaysimplificado.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping
    public ResponseEntity create (@Valid  @RequestBody User user){
try{
       this.userService.createUser(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(user);
       }catch (Exception e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }
    @GetMapping
    public ResponseEntity <List<User>> getAllUsers(){
        List<User> users= this.userService.getAllUsers();
        return new ResponseEntity<>(users,HttpStatus.OK);
    }

    }


