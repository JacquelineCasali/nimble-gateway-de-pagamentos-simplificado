package com.nimble.controller;

        import com.nimble.dto.LoginDto;
        import com.nimble.entity.User;
        import com.nimble.infra.security.DadosTokenJWT;
        import com.nimble.infra.security.TokenService;
        import io.swagger.v3.oas.annotations.Operation;
        import io.swagger.v3.oas.annotations.tags.Tag;
        import jakarta.validation.Valid;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.ResponseEntity;
        import org.springframework.security.authentication.AuthenticationManager;
        import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
        import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")

@Tag(name = "Login", description = "Realizar login Por cpf ou email")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;

    @PostMapping
    @Operation(summary = "Autenticação do usuário", description = "Essa função é responsável por autenticar usuário retornando o JWT")
    public ResponseEntity efetuarLogin(@RequestBody @Valid LoginDto dados) {


        var AutenticacaoToken = new UsernamePasswordAuthenticationToken(dados.login(),dados.senha());
        var autenticacao=authenticationManager.authenticate(AutenticacaoToken);
        var tokenJWT = tokenService.gerarToken((User) autenticacao.getPrincipal());
        return ResponseEntity.ok(new DadosTokenJWT(tokenJWT));
    }

}
