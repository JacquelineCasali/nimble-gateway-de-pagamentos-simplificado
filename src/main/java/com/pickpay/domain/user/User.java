package com.pickpay.domain.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;


import java.math.BigDecimal;

@Entity(name="users")
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
    @Schema(example ="pedro souza" )
    @NotEmpty(message = "Nome não pode ser vazio")
    private String fullName;
       @Column(unique = true)
       @Length(min=11 , message = "O documento deve conter 11 caracteres")
       @Schema(example ="456.456.456-44", minLength = 11 )
    private String cpfCnpj;

    @NotEmpty(message = "Email não pode ser vazio")
    @Email(message = "O campo deve conter um email valido")
    @Column(unique = true)
    @Schema(example ="pedro@gmail.com" )
    private String email;

    @Length(min=3 , message = "A senha deve conter mais de 3 caracteres")

    @Schema(example = "1234",minLength = 3,requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;
// balance saldo do usuario
@Schema(example ="10.55" )

@NotNull(message = "Não pode ser vazio")
private BigDecimal balance ;
    @Schema(example ="COMMON/MERCHANN" )
    @Enumerated(EnumType.STRING)

// tipo de usuario
  private UserType userType;
}
