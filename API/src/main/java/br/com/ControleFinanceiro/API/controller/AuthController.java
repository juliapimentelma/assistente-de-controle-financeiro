package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.LoginRequest;
import br.com.ControleFinanceiro.API.dto.request.UsuarioRequest;
import br.com.ControleFinanceiro.API.dto.response.TokenResponse;
import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.service.AuthService;
import br.com.ControleFinanceiro.API.service.TokenBlacklistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenBlacklistService tokenBlacklistService;

    @PostMapping("/registrar")
    public ResponseEntity<UsuarioResponse> registrar(@RequestBody @Valid UsuarioRequest request) {
        UsuarioResponse response = authService.registrar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody @Valid LoginRequest request) {
        TokenResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String tokenCompleto) {
        String token = tokenCompleto.replace("Bearer ", "");
        tokenBlacklistService.bloquear(token);
        return ResponseEntity.ok(Map.of("mensagem", "Logout realizado com sucesso e token invalidado."));
    }
}
