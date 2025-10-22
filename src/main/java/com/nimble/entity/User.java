package com.nimble.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import java.util.Collection;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Data
@Table(name = "users")
public class User implements UserDetails  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
            private long id;
    @NotBlank(message = "O Nome é obrigatório e não pode estar vazio.")
    private String nome;

    @Column(unique = true)

    private String cpf;

    @NotEmpty(message = "Email não pode ser vazio")
    @Email(message = "O campo deve conter um email valido")
    @Column(unique = true)
    private String email;
    @Length(min=3 , message = "A senha deve conter no minimo 3 caracteres")
    private String senha;
    @NotNull(message = "Não pode ser vazio")
    @Column(nullable = false)
    private BigDecimal saldo = BigDecimal.ZERO;


    // === MÉTODOS OBRIGATÓRIOS DO UserDetails ===
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getPassword() {
        return senha;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


}