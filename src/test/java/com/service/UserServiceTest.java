package com.service;
import com.nimble.entity.User;
import com.nimble.infra.exceptions.MultiplasRegrasException;
import com.nimble.repository.UserRepository;
import com.nimble.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setNome("Anderson Cruz");
        user.setCpf("12345678909");
        user.setEmail("anderson@test.com");
        user.setSenha("123456");
        user.setSaldo(new BigDecimal("1000"));
    }

    @Test
    void testCreateUser_Success() {
        when(userRepository.existsByCpf(anyString())).thenReturn(false);
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(user);

        var response = userService.createUser(user);

        assertEquals(user.getId(), response.id());
        assertEquals(user.getNome(), response.nome());
        assertEquals(user.getCpf(), response.cpf());
        assertEquals(user.getEmail(), response.email());
    }

    @Test
    void testCreateUser_InvalidCpf_ShouldThrow() {
        user.setCpf("111"); // inválido

        MultiplasRegrasException exception = assertThrows(MultiplasRegrasException.class,
                () -> userService.createUser(user));

        // Verifica se a lista de mensagens contém "CPF inválido"
        assertTrue(exception.getMensagens().contains("CPF inválido"));
    }

    @Test
    void testDepositar() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(user));
        userService.depositar(user.getCpf(), new BigDecimal("500"));
        assertEquals(new BigDecimal("1500"), user.getSaldo());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDebitar_Success() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(user));
        userService.debitar(user.getCpf(), new BigDecimal("200"));
        assertEquals(new BigDecimal("800"), user.getSaldo());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDebitar_InsufficientBalance_ShouldThrow() {
        when(userRepository.findByCpf(anyString())).thenReturn(Optional.of(user));
        assertThrows(RuntimeException.class, () -> userService.debitar(user.getCpf(), new BigDecimal("2000")));
    }
}
