package com.nimble.service;


import com.nimble.dto.*;
import com.nimble.entity.Cobranca;
import com.nimble.entity.MetodoPagamento;
import com.nimble.entity.Status;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.RegraNegocioException;
import com.nimble.repository.CobrancaRepository;
import com.nimble.repository.UserRepository;
import com.nimble.util.AutorizadorClient;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class CobrancaService {

    @Autowired
    private UserService userService;
    @Autowired
    private CobrancaRepository cobrancaRepository;

    //    fazer comunicação entre serviços restTemplate
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AutorizadorClient autorizadorClient;
//    @Autowired
//    private AutorizadorCartaoClient autorizadorCartaoClient;
@Transactional
    public CobrancaResponseDto criarCobranca(CobrancaDto cobrancaDto,User originador) throws Exception {

    // Buscar usuários pelo CPF
    User destinatario = userService.findByCpf(cobrancaDto.cpfDestinatario(), "destinatario");

    // Impedir cobrança para o mesmo CPF
    if (originador.getCpf().equals(destinatario.getCpf())) {
        throw new RegraNegocioException("Não é permitido criar uma cobrança para o mesmo CPF.");
    }

    // validando
    userService.validateCobranca(originador, cobrancaDto.valor());

    Cobranca newCobranca = new Cobranca();
    newCobranca.setValor(cobrancaDto.valor());
    newCobranca.setDescricao(cobrancaDto.descricao());
    newCobranca.setOriginador(originador);
    newCobranca.setDestinatario(destinatario);
    newCobranca.setStatus(Status.PENDENTE);
    newCobranca.setDataCriacao(LocalDateTime.now());

    return cobrancaResponse(cobrancaRepository.save(newCobranca));

}

    // listar cobranças enviadas por status do usuario logado
    public List<CobrancaResponseDto> listarCobrancasEnviadas(String cpfOriginador, Status status) throws Exception {
        User originador = userService.findByCpf(cpfOriginador, "originador");
        List<Cobranca> cobrancas = cobrancaRepository.findByOriginadorAndStatus(originador, status);
        return cobrancas.stream().map(this::cobrancaResponse).toList();
    }

    // listar cobranças recebidas por status do usuario logado
    public List<CobrancaResponseDto> listarCobrancasRecebidas(String cpfDestinatario, Status status) throws Exception {
        User destinatario = userService.findByCpf(cpfDestinatario, "destinatario");
        List<Cobranca> cobrancas = cobrancaRepository.findByDestinatarioAndStatus(destinatario, status);
        return cobrancas.stream().map(this::cobrancaResponse).toList();
    }
// pacar cartao ou saldo
    @Transactional
    public CobrancaResponseDto pagarCobranca(PagamentoCartaoDto dto, User pagador) throws Exception {
        Cobranca cobranca = cobrancaRepository.findById(dto.cobrancaId())
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        if (!Objects.equals(cobranca.getDestinatario().getId(), pagador.getId())) {
            throw new RuntimeException("Usuário não autorizado a pagar esta cobrança");
        }

        if (cobranca.getStatus() != Status.PENDENTE) {
            throw new RuntimeException("Só é possível pagar cobranças pendentes");
        }

        BigDecimal valor = cobranca.getValor();
        boolean isCartao = dto.numeroCartao() != null && !dto.numeroCartao().isBlank();

        if (isCartao) {
            System.out.println("Pagamento via CARTÃO detectado.");

            boolean autorizado = autorizadorClient.isAutorizado();
            if (!autorizado) {
                throw new RuntimeException("Pagamento via cartão não autorizado pelo autorizador externo");
            }

            // método de pagamento
            cobranca.setMetodoPagamento(MetodoPagamento.CARTAO);

            User originador = cobranca.getOriginador();
            originador.setSaldo(originador.getSaldo().add(valor));
            userRepository.save(originador);

        } else {
            System.out.println("Pagamento via SALDO detectado.");

            if (pagador.getSaldo().compareTo(valor) < 0) {
                throw new RuntimeException("Saldo insuficiente para pagar a cobrança");
            }

            boolean autorizado = autorizadorClient.isAutorizado();
            if (!autorizado) {
                throw new RuntimeException("Pagamento não autorizado pelo autorizador externo");
            }

            // método de pagamento
            cobranca.setMetodoPagamento(MetodoPagamento.SALDO);

            pagador.setSaldo(pagador.getSaldo().subtract(valor));
            User originador = cobranca.getOriginador();
            originador.setSaldo(originador.getSaldo().add(valor));

            userRepository.save(pagador);
            userRepository.save(originador);
        }

        cobranca.setStatus(Status.PAGA);
        cobrancaRepository.save(cobranca);

        return cobrancaResponse(cobranca);
    }

    @Transactional
    public CobrancaResponseDto cancelarCobranca(Long cobrancaId,User originador) throws Exception {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        // Verifica se o usuário logado é o originador da cobrança
        if (!Objects.equals(cobranca.getOriginador().getId(),originador.getId())) {
            throw new RuntimeException("Usuário não autorizado a cancelar esta cobrança");
        }

//cancelar pendante
                if (cobranca.getStatus() == Status.PENDENTE) {
                    cobranca.setStatus(Status.CANCELADA);
                    cobrancaRepository.save(cobranca);
                    return cobrancaResponse(cobranca);
           }
//cancelar cobrança paga
        if(cobranca.getStatus()==Status.PAGA){
            // Verifica o método de pagamento armazenado na cobrança
            Enum metodoPagamento = cobranca.getMetodoPagamento();
//            User originador = cobranca.getOriginador();
            User pagador = cobranca.getDestinatario();
            BigDecimal valor = cobranca.getValor();
// valor cancelado deve ser estornado, retornando ao saldo do pagador
            if (cobranca.getMetodoPagamento() == MetodoPagamento.SALDO) {
                // 🟩 Estorno local: devolve o valor para o pagador
                originador.setSaldo(originador.getSaldo().subtract(valor));
                pagador.setSaldo(pagador.getSaldo().add(valor));

//                userRepository.save(originador);
                userRepository.save(pagador);
                cobranca.setStatus(Status.CANCELADA);

            } else if (cobranca.getMetodoPagamento() == MetodoPagamento.CARTAO){
                // Chama autorizador externo para validar cancelamento
                boolean autorizado = autorizadorClient.isAutorizado();

                if (!autorizado) {
                    throw new RuntimeException("Cancelamento não autorizado pelo autorizador externo");
                }
// Se autorizado, apenas marca como cancelada
                cobranca.setStatus(Status.CANCELADA);

            }else {
                throw new RuntimeException("Método de pagamento desconhecido");
            }
            cobrancaRepository.save(cobranca);

            return cobrancaResponse(cobranca);
        }

        throw new RuntimeException("Cobrança não pode ser cancelada");


    }




//    @Transactional
//    public CobrancaResponseDto cancelarCobranca(Long cobrancaId,User originador) throws Exception {
//        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
//                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));
//
//        if (cobranca.getStatus() != Status.PENDENTE) {
//            throw new RuntimeException("Só é possível cancelar cobranças pendentes");
//        }
//
//        // Verifica se o usuário logado é o originador da cobrança
//        if (!Objects.equals(cobranca.getOriginador().getId(),originador.getId())) {
//            throw new RuntimeException("Usuário não autorizado a cancelar esta cobrança");
//        }
//        cobranca.setStatus(Status.CANCELADA);
//        cobrancaRepository.save(cobranca);
//
//        return cobrancaResponse(cobranca);
//    }
//


    private CobrancaResponseDto cobrancaResponse(Cobranca cobranca) {


    return new CobrancaResponseDto(
                cobranca.getId(),
                new UserResponseDto(
                        cobranca.getOriginador().getId(),
                        cobranca.getOriginador().getNome(),
                        cobranca.getOriginador().getCpf(),
                        cobranca.getOriginador().getEmail(),
                        cobranca.getOriginador().getSaldo()
                ),
                new UserResponseDto(
                        cobranca.getDestinatario().getId(),
                        cobranca.getDestinatario().getNome(),
                        cobranca.getDestinatario().getCpf(),
                        cobranca.getDestinatario().getEmail(),
                        cobranca.getDestinatario().getSaldo()
                ),
                cobranca.getValor(),
                cobranca.getDescricao(),
                cobranca.getStatus(),
                cobranca.getDataCriacao(),
            cobranca.getMetodoPagamento()
        );
    }

}