package com.nimble.service;

import com.nimble.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    private UserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        // login pode ser CPF ou e-mail

    return repository.findByCpfOrEmail(login,login).orElseThrow(()->new UsernameNotFoundException("Usuário não encontrado"));

    }

}
