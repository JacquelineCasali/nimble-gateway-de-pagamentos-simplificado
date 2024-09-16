package com.pickpaysimplificado.controller;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.pickpaysimplificado.domain.user.User;
import com.pickpaysimplificado.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity create (@Valid  @RequestBody User user){
try{
           var passwordHashred = BCrypt.withDefaults()
                   .hashToString(12, user.getPassword().toCharArray());

           user.setPassword(passwordHashred);

           var userCreated = this.userRepository.save(user);
           return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
       }catch (Exception e){
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
       }
    }
    }


