package com.pickpaysimplificado.domain.transaction;

import com.pickpaysimplificado.domain.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity(name="transactions")
@Table(name = "transactions")
@Getter
@Setter
@AllArgsConstructor
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
private User sender;

    @ManyToOne
    @JoinColumn(name="resolver")
    private User resolver;

    private LocalDateTime timestamp;
}
