package com.nimble.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.nimble.dto.UserResponseDto;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.CpfNotFoundException;
import com.nimble.infra.exceptions.MultiplasRegrasException;
import com.nimble.repository.UserRepository;
import com.nimble.util.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public void validateCobranca(User originador, BigDecimal valor ) throws Exception {

//        validando se tem saldo suficiente para fazer a transferencia

        if(originador.getSaldo().compareTo(valor)<0){
            throw new Exception("Saldo insuficiente");
        }

    }

    // verificando se o cpf exite
    public User findByCpf(String cpf,String campo) throws Exception {
        return this.userRepository.findByCpf(cpf)
                .orElseThrow(() -> new CpfNotFoundException(campo,cpf));
    }


    public UserResponseDto createUser(User user) {
        List<String> erros = new ArrayList<>();

        // remove a formatação
        String cpfLimpo = user.getCpf().replaceAll("\\D", "");
        user.setCpf(cpfLimpo);

        if (!CPFValidator.isValid(cpfLimpo)) {
            erros.add("CPF inválido");
        }

        if (userRepository.existsByCpf(cpfLimpo)) {
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
        User saveUser = userRepository.save(user);

        return new UserResponseDto(saveUser.getId(), saveUser.getNome(), saveUser.getCpf(),
                saveUser.getEmail(),saveUser.getSaldo());
    }
    public void saveUser(User user) {
        this.userRepository.save(user);
    }
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }


    //    public Optional<User> findByCpf(String cpf) {
//        return userRepository.findByCpfOrEmail(cpf, null);
//    }

}
