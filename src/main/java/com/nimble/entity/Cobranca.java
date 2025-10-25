package com.nimble.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
@Entity
@Data
@Table(name = "cobrancas")
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of="id")
public class Cobranca {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //enviar
    //muitos para um
    @ManyToOne(optional = false)
    @JoinColumn(name = "originador_id")
    private User originador;
// quem vai receber a cobrança
@ManyToOne(optional = false)
@JoinColumn(name = "destinatario_id")
private User destinatario;

    @Column(nullable = false)
    private BigDecimal valor;

    private String descricao;

    @Enumerated(EnumType.STRING)
    private Status status = Status.PENDENTE;

    @Enumerated(EnumType.STRING)
    private MetodoPagamento metodoPagamento;


    @CreationTimestamp
    private LocalDateTime dataCriacao;
}

