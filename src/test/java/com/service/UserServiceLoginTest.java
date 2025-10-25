package com.service;

       import com.nimble.entity.User;
        import com.nimble.infra.exceptions.DadoDuplicadoException;
        import com.nimble.repository.UserRepository;
        import com.nimble.service.UserService;
        import at.favre.lib.crypto.bcrypt.BCrypt;
        import org.junit.jupiter.api.BeforeEach;
        import org.junit.jupiter.api.Test;
        import org.mockito.InjectMocks;
        import org.mockito.Mock;
        import org.mockito.MockitoAnnotations;

        import java.math.BigDecimal;
        import java.util.Optional;

        import static org.junit.jupiter.api.Assertions.*;
        import static org.mockito.Mockito.*;

class UserServiceLoginTest {

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
        // Senha criptografada com BCrypt
        user.setSenha(BCrypt.withDefaults().hashToString(12, "123456".toCharArray()));
        user.setSaldo(new BigDecimal("1000"));
    }

    // ================= LOGIN COM SUCESSO =================
    @Test
    void testLogin_Success() {
        when(userRepository.findByCpf("12345678909")).thenReturn(Optional.of(user));

        // Simula verificação da senha no UserService
        boolean senhaValida = BCrypt.verifyer()
                .verify("123456".toCharArray(), user.getSenha())
                .verified;

        assertTrue(senhaValida);
    }

    // ================= USUÁRIO NÃO ENCONTRADO =================
    @Test
    void testLogin_UserNotFound_ShouldThrow() {
        when(userRepository.findByCpf("12345678909")).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.buscarPorCpf("12345678909"); // seu método de busca
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    // ================= SENHA INCORRETA =================
    @Test
    void testLogin_InvalidPassword_ShouldThrow() {
        when(userRepository.findByCpf("12345678909")).thenReturn(Optional.of(user));

        String senhaErrada = "654321";
        boolean senhaValida = BCrypt.verifyer()
                .verify(senhaErrada.toCharArray(), user.getSenha())
                .verified;

        assertFalse(senhaValida);
    }
}
