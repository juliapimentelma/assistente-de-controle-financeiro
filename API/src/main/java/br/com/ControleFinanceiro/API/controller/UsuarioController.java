package br.com.ControleFinanceiro.API.controller;

import br.com.ControleFinanceiro.API.dto.request.AtualizarCredenciaisRequest;
import br.com.ControleFinanceiro.API.dto.request.AtualizarModoEscuroRequest;
import br.com.ControleFinanceiro.API.dto.request.AtualizarPerfilRequest;
import br.com.ControleFinanceiro.API.dto.request.AtualizarTomIaRequest;
import br.com.ControleFinanceiro.API.dto.response.UsuarioResponse;
import br.com.ControleFinanceiro.API.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios/me")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping
    public ResponseEntity<UsuarioResponse> obterPerfil() {
        UsuarioResponse response = usuarioService.obterPerfilLogado();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<UsuarioResponse> atualizarPerfilCompleto(@RequestBody @Valid AtualizarPerfilRequest request) {
        UsuarioResponse response = usuarioService.atualizarPerfilCompleto(request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/credenciais")
    public ResponseEntity<Void> atualizarCredenciais(@RequestBody @Valid AtualizarCredenciaisRequest request) {
        usuarioService.atualizarCredenciais(request);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/desativar")
    public ResponseEntity<Void> desativarConta() {
        usuarioService.desativarConta();
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/tom-ia")
    public ResponseEntity<UsuarioResponse> atualizarTomIa(@RequestBody @Valid AtualizarTomIaRequest request) {
        UsuarioResponse response = usuarioService.atualizarTomIa(request.tomIA());
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/modo-escuro")
    public ResponseEntity<UsuarioResponse> atualizarModoEscuro(@RequestBody @Valid AtualizarModoEscuroRequest request) {
        UsuarioResponse response = usuarioService.atualizarModoEscuro(request.modoEscuro());
        return ResponseEntity.ok(response);
    }
}