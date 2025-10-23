package com.nimble.service;


import com.nimble.dto.CobrancaDto;
import com.nimble.dto.CobrancaResponseDto;
import com.nimble.dto.UserResponseDto;
import com.nimble.entity.Cobranca;
import com.nimble.entity.Status;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.RegraNegocioException;
import com.nimble.repository.CobrancaRepository;
import com.nimble.repository.UserRepository;
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

    @Transactional
    public CobrancaResponseDto pagarCobranca(Long cobrancaId,User pagador) throws Exception {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));



        // Verifica se quem está pagando é o destinatário da cobrança
        if (!Objects.equals(cobranca.getDestinatario().getId(), pagador.getId())) {
            throw new RuntimeException("Usuário não autorizado a pagar esta cobrança");
        }


        if (cobranca.getStatus() != Status.PENDENTE) {
            throw new RuntimeException("Só é possível pagar cobranças pendentes");
        }

// quem vai receber o pagamento
        User originador = cobranca.getOriginador();
        BigDecimal valor = cobranca.getValor();

        // Validar saldo do pagador (destinatário)
        if (pagador.getSaldo().compareTo(valor) < 0) {
            throw new RuntimeException("Saldo insuficiente para pagar a cobrança");
        }

        // Debitar do pagador (destinatário)
        pagador.setSaldo(pagador.getSaldo().subtract(valor));
        // Creditar no originador (quem criou a cobrança)
        originador.setSaldo(originador.getSaldo().add(valor));

        // Atualizar status da cobrança para PAGA
        cobranca.setStatus(Status.PAGA);

        // Salvar alterações
        userRepository.save(originador);
        userRepository.save(pagador);
        cobrancaRepository.save(cobranca);

        return cobrancaResponse(cobranca);
    }

    @Transactional
    public CobrancaResponseDto cancelarCobranca(Long cobrancaId,User originador) throws Exception {
        Cobranca cobranca = cobrancaRepository.findById(cobrancaId)
                .orElseThrow(() -> new RuntimeException("Cobrança não encontrada"));

        if (cobranca.getStatus() != Status.PENDENTE) {
            throw new RuntimeException("Só é possível cancelar cobranças pendentes");
        }



        // Verifica se o usuário logado é o originador da cobrança
        if (!Objects.equals(cobranca.getOriginador().getId(),originador.getId())) {
            throw new RuntimeException("Usuário não autorizado a cancelar esta cobrança");
        }
        cobranca.setStatus(Status.CANCELADA);
        cobrancaRepository.save(cobranca);

        return cobrancaResponse(cobranca);
    }

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
                cobranca.getDataCriacao()
        );
    }}