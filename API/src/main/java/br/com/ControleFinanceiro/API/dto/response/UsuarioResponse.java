package br.com.ControleFinanceiro.API.dto.response;

import br.com.ControleFinanceiro.API.entity.emuns.TomIA;

import java.math.BigDecimal;

public record UsuarioResponse(
        Long id,
        String nome,
        String email,
        BigDecimal saldoAtual,
        Integer scoreFinanceiro,
        TomIA tomIA,
        Boolean modoEscuro,
        Boolean tutorialConcluido
) {}
