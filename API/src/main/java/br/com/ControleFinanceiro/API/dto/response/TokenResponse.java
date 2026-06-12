package br.com.ControleFinanceiro.API.dto.response;

public record TokenResponse(
        String token,
        UsuarioResponse usuario
) {}