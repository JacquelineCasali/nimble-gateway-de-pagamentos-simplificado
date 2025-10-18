package com.nimble.repository;

import com.nimble.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long>{


    boolean existsByCpf(String cpf);
    boolean existsByEmail(String email);
}
