package com.service;

import com.nimble.dto.CobrancaDto;
import com.nimble.dto.PagamentoCartaoDto;
import com.nimble.entity.Cobranca;
import com.nimble.entity.MetodoPagamento;
import com.nimble.entity.Status;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.RegraNegocioException;
import com.nimble.repository.CobrancaRepository;
import com.nimble.repository.UserRepository;
import com.nimble.service.CobrancaService;
import com.nimble.service.UserService;
import com.nimble.util.AutorizadorClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CobrancaServiceTest {

    @Mock
    private UserService userService;

    @Mock
    private CobrancaRepository cobrancaRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AutorizadorClient autorizadorClient;

    @InjectMocks
    private CobrancaService cobrancaService;

    private User originador;
    private User destinatario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        originador = new User();
        originador.setId(1L);
        originador.setCpf("11111111111");
        originador.setSaldo(new BigDecimal("1000"));

        destinatario = new User();
        destinatario.setId(2L);
        destinatario.setCpf("22222222222");
        destinatario.setSaldo(new BigDecimal("500"));
    }

    // ==================== CRIAR COBRANÇA ====================
    @Test
    void testCriarCobrancaSucesso() throws Exception {
        CobrancaDto dto = new CobrancaDto("22222222222", new BigDecimal("100"), "Teste", MetodoPagamento.AGUARDANDO_PAGAMENTO);

        when(userService.findByCpf("22222222222", "destinatario")).thenReturn(destinatario);
        doNothing().when(userService).validateCobranca(originador, dto.valor());
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var response = cobrancaService.criarCobranca(dto, originador);

        assertEquals(0, response.valor().compareTo(dto.valor()));
        assertEquals(Status.PENDENTE, response.status());
        assertEquals(originador.getCpf(), response.originador().cpf());
        assertEquals(destinatario.getCpf(), response.destinatario().cpf());
    }

    @Test
    void testCriarCobrancaMesmoCpfDeveFalhar() throws Exception {
        CobrancaDto dto = new CobrancaDto("11111111111", new BigDecimal("100"), "Teste", MetodoPagamento.AGUARDANDO_PAGAMENTO);

        when(userService.findByCpf("11111111111", "destinatario")).thenReturn(originador);

        RegraNegocioException exception = assertThrows(RegraNegocioException.class, () -> {
            cobrancaService.criarCobranca(dto, originador);
        });

        assertEquals("Não é permitido criar uma cobrança para o mesmo CPF.", exception.getMessage());
    }

    // ==================== PAGAMENTO ====================
    @Test
    void testPagarCobrancaSaldoSucesso() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1L);
        cobranca.setValor(new BigDecimal("200"));
        cobranca.setDestinatario(destinatario);
        cobranca.setOriginador(originador);
        cobranca.setStatus(Status.PENDENTE);

        PagamentoCartaoDto dto = new PagamentoCartaoDto(
                1L,
                cobranca.getValor(),
                null,
                null,
                null,
                null
        );

        when(cobrancaRepository.findById(1L)).thenReturn(Optional.of(cobranca));
        when(autorizadorClient.isAutorizado()).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(i -> i.getArgument(0));

        var response = cobrancaService.pagarCobranca(dto, destinatario);

        assertEquals(Status.PAGA, response.status());
        assertEquals(MetodoPagamento.SALDO, response.metodoPagamento());
        assertEquals(0, destinatario.getSaldo().compareTo(new BigDecimal("300"))); // 500 - 200
        assertEquals(0, originador.getSaldo().compareTo(new BigDecimal("1200")));  // 1000 + 200
    }

    @Test
    void testPagarCobrancaCartaoSucesso() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1L);
        cobranca.setValor(new BigDecimal("150"));
        cobranca.setDestinatario(destinatario);
        cobranca.setOriginador(originador);
        cobranca.setStatus(Status.PENDENTE);

        PagamentoCartaoDto dto = new PagamentoCartaoDto(
                1L,
                cobranca.getValor(),
                "1234123412341234",
                "Teste Titular",
                "12/25",
                "123"
        );

        when(cobrancaRepository.findById(1L)).thenReturn(Optional.of(cobranca));
        when(autorizadorClient.isAutorizado()).thenReturn(true);
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(i -> i.getArgument(0));

        var response = cobrancaService.pagarCobranca(dto, destinatario);

        assertEquals(Status.PAGA, response.status());
        assertEquals(MetodoPagamento.CARTAO, response.metodoPagamento());
        assertEquals(0, originador.getSaldo().compareTo(new BigDecimal("1150"))); // 1000 + 150
        assertEquals(0, destinatario.getSaldo().compareTo(new BigDecimal("500"))); // saldo não muda
    }

    // ==================== CANCELAMENTO ====================
    @Test
    void testCancelarCobrancaPendenteSucesso() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1L);
        cobranca.setStatus(Status.PENDENTE);
        cobranca.setOriginador(originador);
        cobranca.setDestinatario(destinatario); // adicionado para evitar NPE

        when(cobrancaRepository.findById(1L)).thenReturn(Optional.of(cobranca));
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(i -> i.getArgument(0));

        var response = cobrancaService.cancelarCobranca(1L, originador);

        assertEquals(Status.CANCELADA, response.status());
    }

    @Test
    void testCancelarCobrancaPagaSaldoSucesso() throws Exception {
        // Configuração dos usuários com saldo inicial
        originador.setSaldo(new BigDecimal("1200"));
        destinatario.setSaldo(new BigDecimal("500"));

        // Configuração da cobrança
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1L);
        cobranca.setStatus(Status.PAGA);
        cobranca.setMetodoPagamento(MetodoPagamento.SALDO);
        cobranca.setOriginador(originador);
        cobranca.setDestinatario(destinatario);
        cobranca.setValor(new BigDecimal("200"));

        // Mock do repository
        when(cobrancaRepository.findById(1L)).thenReturn(Optional.of(cobranca));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(i -> i.getArgument(0));

        // Executa o método
        var response = cobrancaService.cancelarCobranca(1L, originador);

        // Verifica se a cobrança foi cancelada
        assertEquals(Status.CANCELADA, response.status());

        // Verifica se os saldos foram ajustados corretamente
        assertEquals(0, originador.getSaldo().compareTo(new BigDecimal("1000"))); // 1200 - 200
        assertEquals(0, destinatario.getSaldo().compareTo(new BigDecimal("700"))); // 500 + 200
    }

    @Test
    void testCancelarCobrancaPagaCartaoSucesso() throws Exception {
        Cobranca cobranca = new Cobranca();
        cobranca.setId(1L);
        cobranca.setStatus(Status.PAGA);
        cobranca.setMetodoPagamento(MetodoPagamento.CARTAO);
        cobranca.setOriginador(originador);
        cobranca.setDestinatario(destinatario);
        cobranca.setValor(new BigDecimal("150"));

        when(cobrancaRepository.findById(1L)).thenReturn(Optional.of(cobranca));
        when(autorizadorClient.isAutorizado()).thenReturn(true);
        when(cobrancaRepository.save(any(Cobranca.class))).thenAnswer(i -> i.getArgument(0));

        var response = cobrancaService.cancelarCobranca(1L, originador);

        assertEquals(Status.CANCELADA, response.status());
        // saldo não muda no pagamento via cartão
        assertEquals(0, originador.getSaldo().compareTo(new BigDecimal("1000")));
        assertEquals(0, destinatario.getSaldo().compareTo(new BigDecimal("500")));
    }
}
