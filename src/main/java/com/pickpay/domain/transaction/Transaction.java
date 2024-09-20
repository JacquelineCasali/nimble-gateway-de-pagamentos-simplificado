package com.pickpay.domain.transaction;

import com.pickpay.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name="transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private  Long id;
//    amount valor da transação
    private BigDecimal amount;
//    muitos para um
@ManyToOne
@JoinColumn(name="sender_id")
//enviar
private User sender;

    @ManyToOne
    @JoinColumn(name="receiver_id")
    //receber
    private User receiver;

    private LocalDateTime timestamp;
}
