package com.pickpay.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.pickpay.domain.user.User;
import com.pickpay.domain.user.UserType;

import com.pickpay.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;
    public void validateTransaction(User sender, BigDecimal amount ) throws Exception {
        // validação transação para lojista
        if(sender.getUserType()== UserType.MERCHANN){
    throw new  Exception("Usuario do tipo Lojista não está autorizado a realizar transação");
}
//        validando se tem saldo suficiente para fazer a transferencia
        if(sender.getBalance().compareTo(amount)<0){
            throw new Exception("Saldo insuficiente");
        }

    }
// verificando se o usuario exite

    public User findUserById( Long id) throws Exception {
        return this.userRepository.findUserById(id).orElseThrow(()->new Exception("Usuário não encontrado"));
    }

    public void createUser(User user){
        // criptografando a senha
        var passwordHashred = BCrypt.withDefaults()
                .hashToString(12, user.getPassword().toCharArray());
        user.setPassword(passwordHashred);
        this.userRepository.save(user);
    }

    public void saveUser(User user){
      this.userRepository.save(user);
    }

    // listar todos
    public List<User> getAllUsers(){
        return this.userRepository.findAll();
    }


}
