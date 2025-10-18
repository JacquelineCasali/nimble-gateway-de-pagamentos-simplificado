package com.nimble.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.MultiplasRegrasException;
import com.nimble.repository.UserRepository;
import com.nimble.util.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public void createUser(User user){
        List<String> erros = new ArrayList<>();

        if (!CPFValidator.isValid(user.getCpf())) {
            erros.add("CPF inválido");
        }

        if (userRepository.existsByCpf(user.getCpf())) {
            erros.add("CPF " + user.getCpf() + " já está cadastrado");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            erros.add("E-mail " + user.getEmail() + " já está cadastrado");
        }

        if (!erros.isEmpty()) {
            throw new MultiplasRegrasException(erros);
        }

        // criptografando a senha
        var senhaHashred = BCrypt.withDefaults()
                .hashToString(12, user.getSenha().toCharArray());
        user.setSenha(senhaHashred);
        userRepository.save(user);
    }
}
