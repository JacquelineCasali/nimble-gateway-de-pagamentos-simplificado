package com.pickpaysimplificado.domain.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;



import java.math.BigDecimal;

@Entity(name="users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    private String firstName;
    private  String lastName;

    @Column(unique = true)
    private String document;

    @NotBlank(message = "Email não pode ser vazio")
    @Email(message = "O campo deve conter um email valido")
    @Column(unique = true)
    private String email;
    private String password;
// balance saldo do usuario
private BigDecimal balance;
@Enumerated(EnumType.STRING)
// tipo de usuario
    private UserType userType;
}
