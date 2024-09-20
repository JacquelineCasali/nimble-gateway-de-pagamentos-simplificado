package com.pickpay.repository;

import com.pickpay.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{
    //    Optional para validação
//    Optional<User> findByCpfCnpjOrEmail (String cpfCnpj,String email);
    Optional<User> findUserById (Long id);
}
